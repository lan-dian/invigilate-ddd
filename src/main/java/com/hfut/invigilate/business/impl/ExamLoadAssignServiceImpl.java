package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExamLoadAssignService;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.entity.Invigilate;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exam.DepartmentExamAssignVO;
import com.hfut.invigilate.model.exam.ExamAssignVO;
import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.DepartmentService;
import com.hfut.invigilate.service.ExamService;
import com.hfut.invigilate.service.InvigilateService;
import com.hfut.invigilate.utils.CodeUtils;
import com.hfut.invigilate.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 考试上传分配核心业务类
 */
@Service
@Slf4j
public class ExamLoadAssignServiceImpl implements ExamLoadAssignService {

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExamService examService;

    @Resource
    DepartmentService departmentService;

    @Resource
    ConfigService configService;

    @Override
    public List<ExamConflict> loadExam(List<ExamExcel> examExcels) {
        List<ExamConflict> conflicts = new ArrayList<>();

        Map<String, Integer> departments = departmentService.list().stream()
                .collect(Collectors.toMap(Department::getName, Department::getId));

        Integer money = configService.getInteger(ConfigConst.money);

        List<Exam> exams = convert(examExcels, conflicts, departments, money);

        for (int i = 0; i < exams.size(); i++) {
            Exam exam = exams.get(i);
            try {
                examService.checkTimeConflict(exam);
                boolean save = examService.save(exam);
                if (!save) {
                    log.error("考试保存失败:{}:{}", exam.getName(), JsonUtils.parse(exam));
                    throw new RuntimeException("保存失败");
                }
            } catch (Throwable e) {
                conflicts.add(new ExamConflict(exam.getName(), "excel第" + (i + 2) + "行:" + e.getMessage()));
            }
        }

        return conflicts;
    }


    private List<Exam> convert(List<ExamExcel> examExcels, List<ExamConflict> conflicts, Map<String, Integer> departments, Integer money) {
        examExcels.sort(Comparator.comparing(ExamExcel::getExamTime)
                .thenComparing(ExamExcel::getAddressWithNum));
        List<Exam> exams = new ArrayList<>(examExcels.size());
        boolean needMarge = false;
        Integer studentNum = null;
        Exam exam = null;
        for (ExamExcel excel : examExcels) {

            if (needMarge) {
                exam.setStudentNum(exam.getStudentNum() + excel.getStudentNum());
                exam.setClassDescription(exam.getClassDescription() + "," + excel.getClassDescription());
                needMarge = !exam.getStudentNum().equals(studentNum);
            } else {
                exam = new Exam();
                exam.setCode(CodeUtils.getId());
                exam.setName(excel.getName());
                Matcher dateTimeMatcher = dateTimePattern.matcher(excel.getExamTime());
                if (dateTimeMatcher.find()) {
                    exam.setDate(LocalDate.parse(dateTimeMatcher.group(1), dateFormatter));
                    exam.setStartTime(LocalTime.parse(dateTimeMatcher.group(3), timeFormatter));
                    exam.setEndTime(LocalTime.parse(dateTimeMatcher.group(4), timeFormatter));
                } else {
                    conflicts.add(new ExamConflict(exam.getName(), "日期格式不合法:" + excel.getExamTime()));
                    continue;
                }

                String college = excel.getCollege();
                if (!departments.containsKey(college)) {
                    conflicts.add(new ExamConflict(exam.getName(), "学院:" + college + "不存在,请先添加"));
                    continue;
                }

                Matcher addressMatcher = addressPattern.matcher(excel.getAddressWithNum());
                if (addressMatcher.find()) {
                    exam.setAddress(addressMatcher.group(1));
                    exam.setStudentNum(excel.getStudentNum());
                    studentNum = Integer.valueOf(addressMatcher.group(2));
                } else {
                    conflicts.add(new ExamConflict(exam.getName(), "考试地点(人数)不合法:" + excel.getAddressWithNum()));
                    continue;
                }

                exam.setDepartmentId(departments.get(college));
                exam.setClassDescription(excel.getClassDescription());

                exam.setTeacherName(excel.getTeacherName());
                exam.setMoney(money);

                needMarge = exam.getStudentNum().compareTo(studentNum) < 0;
            }

            if (!needMarge) {
                exam.setTeacherNum(getTeacherNum(exam.getStudentNum()));
                exams.add(exam);
            }
        }


        return exams;
    }

    private static final Pattern dateTimePattern = Pattern.compile("(\\d+年\\d+月(\\d+)日) (\\d+:\\d+)~(\\d+:\\d+)");

    private static final Pattern addressPattern = Pattern.compile("(.*?) \\((\\d+)\\)");

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static Integer getTeacherNum(Integer studentNum) {
        if (studentNum >= 70) {
            return 4;
        } else if (studentNum >= 50) {
            return 3;
        } else {
            return 2;
        }
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void assignWorkIds(List<Integer> workIds, Integer departmentId){
        DepartmentExamAssignVO departmentUnAssignedExam = examService.getDepartmentUnAssignedExam(departmentId);

        Integer needTeacherNum = departmentUnAssignedExam.getNeedTeacherNum();
        if(needTeacherNum==0){
            throw new BusinessException("已经没有可被分配的监考了");
        }

        if(workIds.size()<needTeacherNum){
            throw new BusinessException("供分配的老师少于需求的人数");
        }

        List<ExamAssignVO> exams = departmentUnAssignedExam.getExams();
        ExamAssignVO examExample = exams.get(0);

        List<Integer> banWorkIds = examService.listWorkId(examExample);

        workIds.removeAll(banWorkIds);
        if(workIds.size()<needTeacherNum){
            throw new BusinessException("由于"+banWorkIds+"时间冲突,需要额外添加"+(needTeacherNum-workIds.size())+"人");
        }

        Collections.shuffle(workIds);//打乱顺序

        int index=0;
        for (ExamAssignVO exam : exams) {
            Long examCode = exam.getExamCode();
            int required = exam.getRequiredTeacherNum() - exam.getIndeedTeacherNum();
            for (int i = 0; i < required; i++) {
                Invigilate invigilate = Invigilate.convert(examCode, workIds.get(index));
                index++;
                boolean save = invigilateService.save(invigilate);
                if (!save) {
                    throw new BusinessException("未知原因,分派失败");
                }
            }
        }
    }



}

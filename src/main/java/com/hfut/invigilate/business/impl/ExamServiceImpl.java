package com.hfut.invigilate.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.business.ExamService;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exam.*;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.IDepartmentService;
import com.hfut.invigilate.service.IExamService;
import com.hfut.invigilate.utils.CodeUtils;
import com.hfut.invigilate.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.BagUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExamServiceImpl implements ExamService {

    @Resource
    IDepartmentService iDepartmentService;

    @Resource
    IExamService iExamService;

    @Resource
    ConfigService configService;

    @Override
    public DepartmentExamAssignVO getDepartmentUnAssignedExam(Integer departmentId){
        DepartmentExamAssignVO departmentExamAssignVO = new DepartmentExamAssignVO();
        IPage<ExamAssignVO> iPage=new Page<>(1,Integer.MAX_VALUE);
        iExamService.listRequiredAssignExam(iPage, departmentId);
        List<ExamAssignVO> examAssignVOS = iPage.getRecords();

        int sum=0;
        int index=0;
        LocalDate date=null;
        LocalTime startTime=null;
        LocalTime endTime=null;
        for (ExamAssignVO examAssignVO : examAssignVOS) {
            if(date==null || examAssignVO.getDate().equals(date)){
                if(startTime==null || examAssignVO.getStartTime().equals(startTime)){
                    if(endTime== null || examAssignVO.getEndTime().equals(endTime)){
                        index++;
                        sum+=examAssignVO.getRequiredTeacherNum()-examAssignVO.getIndeedTeacherNum();
                    }else {
                        break;
                    }
                }else {
                    break;
                }
            }else {
                break;
            }
            date=examAssignVO.getDate();
            startTime=examAssignVO.getStartTime();
            endTime=examAssignVO.getEndTime();
        }

        departmentExamAssignVO.setNeedTeacherNum(sum);
        departmentExamAssignVO.setExams(examAssignVOS.subList(0,index));

        return departmentExamAssignVO;
    }

    @Override
    public PageDTO<ExamAssignVO> listRequiredAssignExam(Integer page, Integer limit, Integer departmentId) {
        IPage<ExamAssignVO> iPage=new Page<>(page,limit);
        iExamService.listRequiredAssignExam(iPage, departmentId);
        return PageDTO.build(iPage);
    }


    @Override
    public PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query) {
        return iExamService.page(page, limit, query);
    }

    @Override
    public List<ExamConflict> loadExam(List<ExamExcel> examExcels) {
        List<ExamConflict> conflicts = new ArrayList<>();

        Map<String, Integer> departments = iDepartmentService.list().stream()
                .collect(Collectors.toMap(Department::getName, Department::getId));

        Integer money = configService.getInteger(ConfigConst.money);

        List<Exam> exams = convert(examExcels, conflicts, departments, money);

        for (int i = 0; i < exams.size(); i++) {
            Exam exam = exams.get(i);
            try {
                iExamService.checkTimeConflict(exam);
                boolean save = iExamService.save(exam);
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


}

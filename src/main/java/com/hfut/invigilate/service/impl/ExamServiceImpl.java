package com.hfut.invigilate.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.mapper.ExamMapper;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exam.*;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.DepartmentService;
import com.hfut.invigilate.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfut.invigilate.utils.CodeUtils;
import com.hfut.invigilate.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 考试 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
@Slf4j
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Resource
    DepartmentService departmentService;

    @Resource
    ConfigService configService;

    @Override
    public DepartmentExamAssignVO getDepartmentUnAssignedExam(Integer departmentId){
        DepartmentExamAssignVO departmentExamAssignVO = new DepartmentExamAssignVO();
        PageDTO<ExamAssignVO> examAssignVOPageDTO = listRequiredAssignExam(1, Integer.MAX_VALUE, departmentId);
        List<ExamAssignVO> examAssignVOS = examAssignVOPageDTO.getData();

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
        baseMapper.listAssignVO(iPage,departmentId);
        return PageDTO.build(iPage);
    }




    @Override
    public void checkTimeConflict(Exam exam){
        List<Exam> exams = lambdaQuery()
                .eq(Exam::getDate, exam.getDate())
                .eq(Exam::getAddress,exam.getAddress())
                .list();
        if(exams.isEmpty()){
            return;
        }
        for (Exam originExam : exams) {
            originExam.checkTimeConflict(exam);
        }
    }

    @Override
    public PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query){
        List<ExamTeachersVO> examTeachersVOS = this.baseMapper.page((page - 1) * limit, limit, query);
        Long n = this.baseMapper.count(query);
        return PageDTO.build(n,examTeachersVOS);
    }

    @Override
    public List<Integer> listWorkId(ExamAssignVO examAssignVO) {
        return baseMapper.listWorkId(examAssignVO.getDate(),examAssignVO.getStartTime(),examAssignVO.getEndTime());
    }

}

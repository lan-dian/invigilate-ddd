package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExamService;
import com.hfut.invigilate.entity.Config;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.IDepartmentService;
import com.hfut.invigilate.service.IExamService;
import com.hfut.invigilate.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<ExamConflict> loadExam(List<ExamExcel> examExcels){
        List<ExamConflict> conflicts=new ArrayList<>();

        Map<String, Integer> departments = iDepartmentService.list().stream()
                .collect(Collectors.toMap(Department::getName, Department::getId));

        Integer money = configService.getInteger(ConfigConst.money);

        for (int i = 0; i < examExcels.size(); i++) {
            ExamExcel examExcel=examExcels.get(i);
            try {
                Exam exam = Exam.convert(examExcel, departments, money);

                iExamService.checkTimeConflict(exam);

                boolean save = iExamService.save(exam);
                if(!save){
                    log.error("考试保存失败:{}:{}",examExcel.getName(),JsonUtils.parse(exam));
                    throw new RuntimeException("保存失败");
                }
            }catch (Throwable e){
                conflicts.add(new ExamConflict(examExcel.getName(),"excel第"+(i+2)+"行:"+ e.getMessage()));
            }
        }

        return conflicts;
    }


}

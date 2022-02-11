package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExamService;
import com.hfut.invigilate.entity.Config;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exam.ExamBO;
import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.IDepartmentService;
import com.hfut.invigilate.service.IExamService;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        for (ExamExcel examExcel : examExcels) {
            try {
                Exam exam = Exam.convert(examExcel, departments, money);
                /*boolean save = iExamService.save(exam);
                if(!save){
                    throw new RuntimeException("保存失败");
                }*/
            }catch (Throwable e){
                conflicts.add(new ExamConflict(examExcel.getName(),e.getMessage()));
            }
        }





        return conflicts;
    }


}

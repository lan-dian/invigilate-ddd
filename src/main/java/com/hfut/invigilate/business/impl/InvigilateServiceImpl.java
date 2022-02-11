package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExamService;
import com.hfut.invigilate.business.InvigilateService;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.entity.Invigilate;
import com.hfut.invigilate.model.exam.DepartmentExamAssignVO;
import com.hfut.invigilate.model.exam.ExamAssignVO;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.IExamService;
import com.hfut.invigilate.service.IInvigilateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class InvigilateServiceImpl implements InvigilateService {

    @Resource
    IInvigilateService iInvigilateService;

    @Resource
    ExamService examService;

    @Resource
    IExamService iExamService;

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

        List<Integer> banWorkIds = iExamService.listWorkId(examExample);

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
                boolean save = iInvigilateService.save(invigilate);
                if (!save) {
                    throw new BusinessException("未知原因,分派失败");
                }
            }
        }
    }


}

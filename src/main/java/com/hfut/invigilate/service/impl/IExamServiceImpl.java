package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.mapper.ExamMapper;
import com.hfut.invigilate.service.IExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 考试 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class IExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements IExamService {

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

}

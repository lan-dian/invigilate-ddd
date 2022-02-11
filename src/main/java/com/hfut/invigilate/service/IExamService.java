package com.hfut.invigilate.service;

import com.hfut.invigilate.entity.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.ExamPageQueryDTO;
import com.hfut.invigilate.model.exam.ExamTeachersVO;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * 考试 服务类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface IExamService extends IService<Exam> {

    void checkTimeConflict(Exam exam);

    PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query);
}

package com.hfut.invigilate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hfut.invigilate.entity.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.*;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * 考试 服务类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface ExamService extends IService<Exam> {

    void checkTimeConflict(Exam exam);

    List<Exam> listConflictExam(Exam exam, Integer workId);

    Exam getByCode(Long code);

    DepartmentExamAssignVO getDepartmentUnAssignedExam(Integer departmentId);

    PageDTO<ExamAssignVO> listRequiredAssignExam(Integer page, Integer limit, Integer departmentId);

    PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query);

    List<Integer> listWorkId(ExamAssignVO examAssignVO);
}

package com.hfut.invigilate.business;

import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.*;

import java.util.List;

public interface ExamService {

    List<ExamConflict> loadExam(List<ExamExcel> examExcels);

    DepartmentExamAssignVO getDepartmentUnAssignedExam(Integer departmentId);

    PageDTO<ExamAssignVO> listRequiredAssignExam(Integer page, Integer limit, Integer departmentId);

    PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query);

}

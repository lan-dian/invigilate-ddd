package com.hfut.invigilate.business;

import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.model.exam.ExamPageQueryDTO;
import com.hfut.invigilate.model.exam.ExamTeachersVO;

import java.util.List;

public interface ExamService {

    List<ExamConflict> loadExam(List<ExamExcel> examExcels);

    PageDTO<ExamTeachersVO> page(Integer page, Integer limit, ExamPageQueryDTO query);

}

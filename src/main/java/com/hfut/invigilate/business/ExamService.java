package com.hfut.invigilate.business;

import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;

import java.util.List;

public interface ExamService {

    List<ExamConflict> loadExam(List<ExamExcel> examExcels);

}

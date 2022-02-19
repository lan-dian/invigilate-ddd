package com.hfut.invigilate.business;

import com.hfut.invigilate.model.exam.err.ConflictList;
import com.hfut.invigilate.model.exam.err.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;

import java.util.List;

public interface ExamLoadAssignService {

    ConflictList loadExam(List<ExamExcel> examExcels);

    void assignWorkIds(List<Integer> workIds, Integer departmentId);

}

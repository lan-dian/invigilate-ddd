package com.hfut.invigilate.business;

import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Set;

public interface ExamLoadAssignService {

    List<ExamConflict> loadExam(List<ExamExcel> examExcels);

    void assignWorkIds(List<Integer> workIds, Integer departmentId);

}

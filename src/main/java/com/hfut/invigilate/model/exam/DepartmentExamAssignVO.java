package com.hfut.invigilate.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class DepartmentExamAssignVO {

    @ApiModelProperty("需要的老师人数")
    private Integer needTeacherNum;

    @ApiModelProperty("考试")
    private List<ExamAssignVO> exams;

}

package com.hfut.invigilate.model.exam;

import com.hfut.invigilate.entity.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ExamAssignVO {

    @ApiModelProperty("考试编码")
    private Long examCode;

    @ApiModelProperty("考试名称")
    private String name;

    @ApiModelProperty("考试日期")
    private LocalDate date;

    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "需要的监考人数")
    private Integer requiredTeacherNum;

    @ApiModelProperty(value = "实际的监考人数")
    private Integer indeedTeacherNum;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty(value = "考试地点")
    private String address;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "工资")
    private Integer money;



}

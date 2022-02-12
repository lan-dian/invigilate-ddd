package com.hfut.invigilate.model.exam;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ExamVO {

    @ApiModelProperty(value = "考试编码")
    private Long examCode;

    @ApiModelProperty(value = "考试名称")
    private String name;

    @ApiModelProperty(value = "考试日期")
    private LocalDate date;

    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "监考人数")
    private Integer teacherNum;

    @ApiModelProperty(value = "考试地点")
    private String address;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "教学班描述")
    private String classDescription;

    @ApiModelProperty(value = "学生人数")
    private Integer studentNum;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "工资")
    private Integer money;

}

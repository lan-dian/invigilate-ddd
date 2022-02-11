package com.hfut.invigilate.model.exam;

import com.hfut.invigilate.utils.CodeUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 考试
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Data
@NoArgsConstructor
public class ExamBO {

    @ApiModelProperty(value = "考试编码")
    private Long code;

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

    @ApiModelProperty(value = "部门id")
    private Integer departmentId;

    @ApiModelProperty(value = "教学班描述")
    private String classDescription;

    @ApiModelProperty(value = "学生人数")
    private Integer studentNum;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "工资")
    private Integer money;




}

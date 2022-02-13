package com.hfut.invigilate.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ExamSimpleVO {

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

    @ApiModelProperty(value = "考试地点")
    private String address;

    @ApiModelProperty(value = "工资")
    private Integer money;

}

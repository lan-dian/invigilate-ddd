package com.hfut.invigilate.model.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class IntendExamVO {

    @ApiModelProperty("监考编码")
    private Long invigilateCode;

    @ApiModelProperty("交换编码")
    private Long exchangeCode;

    @ApiModelProperty("考试编码")
    private Long examCode;

    @ApiModelProperty("考试时间")
    private LocalDate date;

    @ApiModelProperty("开始时间")
    private LocalTime startTime;

    @ApiModelProperty("结束时间")
    private LocalTime endTime;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("考场")
    private String address;

    @ApiModelProperty("工资")
    private Integer money;

}
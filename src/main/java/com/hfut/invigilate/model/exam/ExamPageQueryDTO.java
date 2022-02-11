package com.hfut.invigilate.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ExamPageQueryDTO {

    @ApiModelProperty("考试编码")
    private Long code;

    @ApiModelProperty("考试名称")
    private String examName;

    @ApiModelProperty("开始日期")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    private LocalDate endDate;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("教师工号")
    private Integer workId;

}
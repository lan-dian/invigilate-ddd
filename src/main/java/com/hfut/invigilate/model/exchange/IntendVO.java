package com.hfut.invigilate.model.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IntendVO {

    @ApiModelProperty("工号")
    private Integer workId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("可交换的考试")
    private List<IntendExamVO> exams;

}
package com.hfut.invigilate.model.exchange;

import com.hfut.invigilate.model.exam.ExamSimpleVO;
import com.hfut.invigilate.model.user.UserSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvigilateExchangeVO {

    @ApiModelProperty("交换意图")
    private String msg;

    @ApiModelProperty("监考编码")
    private Long invigilateCode;

    @ApiModelProperty("提出交换的老师")
    private UserSimpleVO teacher;

    @ApiModelProperty("想要被交换的监考")
    private ExamSimpleVO exam;

}
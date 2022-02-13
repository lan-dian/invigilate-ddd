package com.hfut.invigilate.model.exchange;

import com.hfut.invigilate.model.exam.ExamSimpleVO;
import com.hfut.invigilate.model.exam.ExamVO;
import com.hfut.invigilate.model.user.UserInfoVO;
import com.hfut.invigilate.model.user.UserSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelfExchangeIntendVO {

    @ApiModelProperty("交换id")
    private Long exchangeCode;

    @ApiModelProperty("自己的考试")
    private ExamSimpleVO self;

    @ApiModelProperty("目标交换考试")
    private ExamSimpleVO target;

    @ApiModelProperty("目标交换人物")
    private UserSimpleVO targetTeacher;

}
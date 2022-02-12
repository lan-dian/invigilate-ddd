package com.hfut.invigilate.model.exchange;

import com.hfut.invigilate.model.exam.ExamVO;
import com.hfut.invigilate.model.invigilate.ExamStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WantToBeExchangeInvigilate {

    @ApiModelProperty("监考")
    private Long invigilateCode;

    @ApiModelProperty("监考状态")
    private ExamStateEnum state;

    @ApiModelProperty("想要和我交换考试的人数")
    private Integer exchangeNum;

    @ApiModelProperty("交换期望")
    private String msg;

    @ApiModelProperty("考试")
    private ExamVO exam;

    public void confirmState(){
        if(exchangeNum>0){
            state=ExamStateEnum.TO_BE_CONFIRMED;
        }else {
            state=ExamStateEnum.TO_BE_REPLACED;
        }
    }

}
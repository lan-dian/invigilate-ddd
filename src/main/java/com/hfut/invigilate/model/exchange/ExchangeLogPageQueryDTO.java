package com.hfut.invigilate.model.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExchangeLogPageQueryDTO {

    @ApiModelProperty("工号")
    private Integer workId;

    @ApiModelProperty("考试编号")
    private Long examCode;

    @ApiModelProperty("仅仅显示对结果有影响的记录")
    private Boolean onlyShowEssential;

    @ApiModelProperty("交换类别")
    private ExchangeType exchangeType;

}

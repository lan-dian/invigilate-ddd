package com.hfut.invigilate.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInvigilateVO {

    @ApiModelProperty("监考编码")
    private Long invigilateCode;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("工号")
    private Integer workId;

    @ApiModelProperty("电话")
    private String telephone;

}

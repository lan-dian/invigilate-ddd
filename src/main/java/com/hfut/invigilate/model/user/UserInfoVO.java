package com.hfut.invigilate.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoVO {

    @ApiModelProperty(value = "工号")
    private Integer workId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty("部门名称")
    private String department;

}

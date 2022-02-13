package com.hfut.invigilate.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPageQueryDTO {

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("工号")
    private String workId;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("部门")
    private String college;

    @ApiModelProperty("角色")
    private RoleEnum role;

}
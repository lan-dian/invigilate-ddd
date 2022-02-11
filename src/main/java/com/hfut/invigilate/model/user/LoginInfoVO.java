package com.hfut.invigilate.model.user;

import io.swagger.annotations.ApiModelProperty;
import javafx.scene.effect.SepiaTone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoVO {

    @ApiModelProperty("Bearer token 有效部分")
    private String token;

    @ApiModelProperty("角色")
    private Set<RoleEnum> roles;

    @ApiModelProperty("姓")
    private String name;

}

package com.hfut.invigilate.model.user;

import com.landao.checker.annotations.Check;
import com.landao.checker.annotations.special.TelePhone;
import com.landao.checker.annotations.special.group.Id;
import com.landao.checker.core.Checked;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserAdminVO implements Checked {

    @Check("工号")
    @ApiModelProperty(value = "工号")
    private Integer workId;

    @Check(value = "姓名",max = 10)
    @ApiModelProperty(value = "姓名")
    private String name;

    @TelePhone
    @ApiModelProperty(value = "电话")
    private String telephone;

    @Check("学院id")
    @ApiModelProperty("学院id")
    private Integer departmentId;

    @ApiModelProperty("角色")
    private Set<RoleEnum> roles;


    @Override
    public void check(Class<?> group, String supperName) {
        if(roles.isEmpty()){
            throwIllegal("roles","请至少选择一个角色");
        }
    }

}

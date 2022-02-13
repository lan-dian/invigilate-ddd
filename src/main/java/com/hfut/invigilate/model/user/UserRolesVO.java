package com.hfut.invigilate.model.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Data
@NoArgsConstructor
public class UserRolesVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "工号")
    private Integer workId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("学院")
    private String college;

    @ApiModelProperty("角色")
    private List<RoleEnum> roles;

}

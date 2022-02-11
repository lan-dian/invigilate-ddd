package com.hfut.invigilate.model.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDepartmentVO {

    @ApiModelProperty(value = "工号")
    private Integer workId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty("部门名称")
    private String department;

    @ApiModelProperty("监考次数")
    private Integer invigilateNum;

}

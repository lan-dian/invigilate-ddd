package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hfut.invigilate.utils.CodeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监考
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Invigilate对象", description="监考")
public class Invigilate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "工号")
    private Integer workId;

    @ApiModelProperty(value = "考试编码")
    private Long examCode;

    @ApiModelProperty(value = "想要交换的数量")
    private Integer exchangeNum;

    @ApiModelProperty(value = "监考编码")
    private Long code;

    @ApiModelProperty(value = "交换信息")
    private String msg;

    public static Invigilate convert(Long examCode,Integer workId){
        Invigilate invigilate = new Invigilate();
        invigilate.setWorkId(workId);
        invigilate.setExamCode(examCode);
        invigilate.setExchangeNum(0);
        invigilate.setCode(CodeUtils.getId());
        invigilate.setMsg("");
        return invigilate;
    }


}

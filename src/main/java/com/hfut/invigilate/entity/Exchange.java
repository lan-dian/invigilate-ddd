package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 交换记录
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Exchange对象", description="交换记录")
public class Exchange implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发起人工号")
    private Integer requestWorkId;

    @ApiModelProperty(value = "预交换考试")
    private Long requestExamCode;

    @ApiModelProperty(value = "预交换的监考")
    private Long requestInvigilateCode;

    @ApiModelProperty(value = "响应者工号")
    private Integer responseWorkId;

    @ApiModelProperty(value = "待交换考试")
    private Long responseExamCode;

    @ApiModelProperty(value = "待交换监考")
    private Long responseInvigilateCode;

    @ApiModelProperty(value = "交换状态")
    private Integer state;


}

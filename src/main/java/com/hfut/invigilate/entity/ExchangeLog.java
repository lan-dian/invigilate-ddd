package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.hfut.invigilate.model.exchange.ExchangeState;
import com.hfut.invigilate.model.exchange.ExchangeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Accessors(chain = true)
@ApiModel(value="ExchangeLog对象", description="交换记录")
public class ExchangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人")
    private Integer workId;

    @ApiModelProperty(value = "预交换的考试")
    private Long requestExamCode;

    @ApiModelProperty(value = "待交换的考试")
    private Long responseExamCode;

    @ApiModelProperty(value = "描述")
    private String detail;

    @ApiModelProperty(value = "0:过程,1:结果")
    private ExchangeState state;

    @ApiModelProperty("交换类型")
    private ExchangeType exchangeType;


}

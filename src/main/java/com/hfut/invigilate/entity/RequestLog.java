package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.hfut.invigilate.model.commen.RequestLogDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请求日志
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RequestLog对象", description="请求日志")
public class RequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "单位毫秒")
    private Integer timeCost;

    @ApiModelProperty(value = "工号")
    private Integer workId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "方法签名")
    private String signature;

    @ApiModelProperty(value = "请求参数")
    private String args;

    private String data;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String msg;

    @ApiModelProperty(value = "http状态码")
    private Integer status;

    @ApiModelProperty(value = "成功与否")
    private Boolean success;

    public static RequestLog convert(RequestLogDTO requestLogDTO){
        RequestLog requestLog = new RequestLog();
        requestLog.setCreateTime(requestLogDTO.getCreateTime());
        requestLog.setTimeCost(requestLogDTO.getTimeCost());
        requestLog.setWorkId(requestLogDTO.getWorkId());
        requestLog.setName(requestLogDTO.getName());
        requestLog.setUrl(requestLogDTO.getUrl());
        requestLog.setSignature(requestLogDTO.getSignature());
        requestLog.setArgs(requestLogDTO.getArgs());
        requestLog.setData(requestLogDTO.getData());
        requestLog.setCode(requestLogDTO.getCode());
        requestLog.setMsg(requestLogDTO.getMsg());
        requestLog.setStatus(requestLogDTO.getStatus());
        requestLog.setSuccess(requestLogDTO.getSuccess());
        return requestLog;
    }


}

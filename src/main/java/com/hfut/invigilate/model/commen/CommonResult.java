package com.hfut.invigilate.model.commen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一返回结果
 */
@Data
@Accessors(chain = true)
public class CommonResult<T> {

    @ApiModelProperty(value = "状态码",notes = "0:成功,-1:失败(将msg显示给用户),-999:系统错误")
    private Integer code;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    public CommonResult() {
        this.code = 0;
        this.msg = "成功";
        this.data = null;
    }

    public CommonResult(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResult<T> ok(){
        this.code=0;
        this.msg="成功";
        this.data=null;
        return this;
    }

    public CommonResult<T> ok(String msg){
        this.code=0;
        this.msg=msg;
        this.data=null;
        return this;
    }

    public CommonResult<T> body(T data){
        this.code=0;
        this.msg="成功";
        this.data=data;
        return this;
    }

    public CommonResult<T> body(T data,String msg){
        this.code=0;
        this.msg=msg;
        this.data=data;
        return this;
    }

    public CommonResult<T> err(String msg){
        this.code=-1;
        this.msg=msg;
        this.data=null;
        return this;
    }

    public CommonResult<T> err(String msg,T data){
        this.code=-1;
        this.msg=msg;
        this.data=data;
        return this;
    }

    public CommonResult<T> err(){
        this.code=-1;
        this.msg="失败";
        this.data=null;
        return this;
    }

    public CommonResult<T> err(String msg,Integer code){
        this.code=code;
        this.msg=msg;
        this.data=null;
        return this;
    }

    public CommonResult<T> ok(boolean success){
        if(success){
            this.code=0;
            this.msg="成功";
        }else {
            this.code=-1;
            this.msg="失败";
        }
        this.data=null;
        return this;
    }

    public CommonResult<T> ok(boolean success, String msg) {
        if(success){
            this.code=0;
            this.msg="成功";
        }else {
            this.code=-1;
            this.msg=msg;
        }
        this.data=null;
        return this;
    }

}

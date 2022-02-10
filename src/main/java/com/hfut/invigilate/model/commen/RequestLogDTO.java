package com.hfut.invigilate.model.commen;


import com.hfut.invigilate.author.UserTokenBean;
import com.hfut.invigilate.utils.HfutWebUtils;
import com.hfut.invigilate.utils.JsonUtils;
import com.landao.guardian.core.GuardianContext;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class RequestLogDTO {

    private RequestLogDTO() {

    }

    public static final ThreadLocal<RequestLogDTO> REQUEST_LOG = new ThreadLocal<>();

    public static RequestLogDTO getCurrent() {
        RequestLogDTO requestLogDTO = REQUEST_LOG.get();
        if (requestLogDTO == null) {
            requestLogDTO = new RequestLogDTO();
            REQUEST_LOG.set(requestLogDTO);
        }
        return requestLogDTO;
    }

    public static void startLog(ProceedingJoinPoint point) {
        RequestLogDTO logDTO = getCurrent();
        HttpServletRequest request = HfutWebUtils.getRequest();
        //设置请求url
        logDTO.url = request.getRequestURI();
        //设置类名和方法名
        Signature signature = point.getSignature();
        logDTO.signature = signature.getDeclaringType().getSimpleName() + "#" + signature.getName();
        //设置用户信息
        if (GuardianContext.isLogin()) {
            UserTokenBean user = GuardianContext.getUser(UserTokenBean.class);
            logDTO.workId = user.getWorkId();
            logDTO.name = user.getName();
        }
        //设置参数
        StringBuilder arg = new StringBuilder();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = point.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            arg.append(parameterName).append("=").append(JsonUtils.parse(args[i]));
        }
        logDTO.args=arg.toString();
        logDTO.createTime = LocalDateTime.now();
    }

    public static RequestLogDTO endLog(CommonResult<Object> result){
        RequestLogDTO logDTO = getCurrent();
        long start = logDTO.createTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        logDTO.timeCost= Math.toIntExact(System.currentTimeMillis() - start);
        logDTO.code=result.getCode();
        logDTO.msg=result.getMsg();
        logDTO.data=JsonUtils.parse(result.getData());
        HttpServletResponse response = HfutWebUtils.getResponse();
        logDTO.status= response.getStatus();
        logDTO.success= logDTO.code == 0;
        return logDTO;
    }

    public static void clear(){
        REQUEST_LOG.remove();
    }

    private LocalDateTime createTime;

    private Integer timeCost;

    private Integer workId;

    private String name;

    private String url;

    private String signature;

    private String args;

    private String data;

    private Integer code;

    private String msg;

    private Integer status;

    private Boolean success;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Integer timeCost) {
        this.timeCost = timeCost;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

package com.hfut.invigilate.config;


import com.hfut.invigilate.entity.RequestLog;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.RequestLogDTO;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.RequestLogService;
import com.hfut.invigilate.utils.HfutWebUtils;
import com.landao.checker.model.collection.IllegalsHolder;
import com.landao.checker.model.exception.CheckIllegalException;
import com.landao.guardian.exception.author.UnAuthorizationException;
import com.landao.guardian.exception.author.UnLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private RequestLogService requestLogService;

    /**
     * 系统异常
     */
    @ExceptionHandler(Throwable.class)
	public CommonResult<Object> exceptionHandler(Throwable e){
        CommonResult<Object> result = new CommonResult<>();
        if(e instanceof UnLoginException){
            result.err(e.getMessage());
            HfutWebUtils.setResponseStatus(HttpStatus.UNAUTHORIZED);
        }else if(e instanceof UnAuthorizationException){
            result.err(e.getMessage());
            HfutWebUtils.setResponseStatus(HttpStatus.FORBIDDEN);
        }else if(e instanceof BusinessException){
            BusinessException err=(BusinessException) e;
            result.setCode(err.getCode())
                    .setMsg(err.getMsg())
                    .setData(err.getData());
        }else if(e instanceof CheckIllegalException){
            CheckIllegalException err=(CheckIllegalException) e;
            result.err("参数错误",err.getIllegalList());
            HfutWebUtils.setResponseStatus(HttpStatus.BAD_REQUEST);
        }else {
            for (StackTraceElement element : e.getStackTrace()) {
                if(element.getClassName().startsWith("com.hfut")){
                    log.error("类:{} 方法:{} 行:{}",element.getClassName(),element.getMethodName(),element.getLineNumber());
                }
            }
            result.err(e.getMessage());
        }

        //处理日志
        RequestLogDTO requestLogDTO = RequestLogDTO.endLog(result);
        RequestLog requestLog = RequestLog.convert(requestLogDTO);
        try {
            requestLogService.save(requestLog);
        }catch (Throwable err){
            log.warn("日志保存时异常:{}",result);
            err.printStackTrace();
        }
        RequestLogDTO.clear();

        return result;
    }

}
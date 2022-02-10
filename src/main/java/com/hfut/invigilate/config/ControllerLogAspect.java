package com.hfut.invigilate.config;


import com.hfut.invigilate.entity.RequestLog;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.RequestLogDTO;
import com.hfut.invigilate.service.RequestLogService;
import com.hfut.invigilate.service.impl.RequestLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 控制器日志切面
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

    @Resource
    private RequestLogService requestLogService;


    @Around("execution(public * com.hfut.invigilate.controller.*.*(..))")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestLogDTO.clear();

        RequestLogDTO.startLog(proceedingJoinPoint);

        Object result=null;

        try {
            result=proceedingJoinPoint.proceed();
        }finally {
            if(result!=null){
                try {
                    CommonResult<Object> commonResult = (CommonResult<Object>) result;
                    RequestLogDTO requestLogDTO = RequestLogDTO.endLog(commonResult);
                    RequestLog requestLog = RequestLog.convert(requestLogDTO);
                    requestLogService.save(requestLog);
                    RequestLogDTO.clear();
                }catch (Throwable e){
                    log.warn("日志保存时异常:{}",result);
                    e.printStackTrace();
                }
            }
        }

       return result;
    }


}

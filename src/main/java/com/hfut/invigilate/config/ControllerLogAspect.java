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
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.logging.Handler;

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

        return proceedingJoinPoint.proceed();
    }


}

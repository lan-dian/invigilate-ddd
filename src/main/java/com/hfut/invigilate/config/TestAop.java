package com.hfut.invigilate.config;

import com.hfut.invigilate.model.commen.RequestLogDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TestAop {

    @Before("execution(public com.hfut.invigilate.model.commen.CommonResult<*> *.*.controller.*(..))")
    public void startLog(JoinPoint joinPoint) throws Throwable {
        RequestLogDTO.clear();
    }


}

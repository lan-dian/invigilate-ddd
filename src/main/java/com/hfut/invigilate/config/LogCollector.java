package com.hfut.invigilate.config;

import com.hfut.invigilate.entity.RequestLog;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.RequestLogDTO;
import com.hfut.invigilate.service.RequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.stream.Collector;

@Slf4j
@RestControllerAdvice
public class LogCollector implements ResponseBodyAdvice<Object> {

    @Resource
    private RequestLogService requestLogService;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof CommonResult){
            CommonResult result= (CommonResult) body;
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
        }
        return body;
    }
}

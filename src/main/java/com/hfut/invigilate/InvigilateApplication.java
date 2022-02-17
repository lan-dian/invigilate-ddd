package com.hfut.invigilate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.test.BeanPost;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import javax.annotation.Resource;

@Slf4j
@SpringBootApplication
@MapperScan("com.hfut.invigilate.mapper")
public class InvigilateApplication {


    public static void main(String[] args) throws JsonProcessingException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(InvigilateApplication.class, args);
        System.out.println(applicationContext.getId());
        log.info("http://localhost:8005/doc.html");
    }

}

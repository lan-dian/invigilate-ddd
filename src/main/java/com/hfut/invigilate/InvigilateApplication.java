package com.hfut.invigilate;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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

package com.hfut.invigilate;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableSwaggerBootstrapUI
@MapperScan("com.hfut.invigilate.mapper")
public class InvigilateApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvigilateApplication.class, args);
        log.info("http://localhost:8005/doc.html");
    }

}

package com.hfut.invigilate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hfut.invigilate.mapper")
public class InvigilateApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvigilateApplication.class, args);

    }

}

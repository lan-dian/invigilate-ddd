package com.hfut.invigilate;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.hfut.invigilate.mapper")
@EnableTransactionManagement
public class InvigilateApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvigilateApplication.class, args);
        log.info("http://localhost:8005/doc.html");
    }

}

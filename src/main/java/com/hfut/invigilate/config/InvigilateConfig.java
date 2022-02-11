package com.hfut.invigilate.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("invigilate")
public class InvigilateConfig {

    private Integer machineId=0;

}

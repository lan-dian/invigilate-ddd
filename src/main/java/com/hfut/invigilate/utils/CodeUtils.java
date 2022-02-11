package com.hfut.invigilate.utils;

import com.hfut.invigilate.config.InvigilateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeUtils {

    private static InvigilateConfig invigilateConfig;

    private static IdWorker idWorker;

    @Autowired
    public void setInvigilateConfig(InvigilateConfig invigilateConfig){
        CodeUtils.invigilateConfig=invigilateConfig;
        idWorker=new IdWorker(invigilateConfig.getMachineId(),0);
    }


    public static Long getId(){
        return idWorker.nextId();
    }



}

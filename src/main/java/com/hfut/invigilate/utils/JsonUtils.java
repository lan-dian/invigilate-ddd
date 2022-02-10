package com.hfut.invigilate.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 把对象转换为json字符串
 */
@Component//必须加这个注解让spring扫描到
public class JsonUtils {

    public static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper){
        JsonUtils.objectMapper=objectMapper;
    }

    public static String parse(Object obj){
        if(obj==null){
            return "null";
        }
        String res=null;
        try {
            res=objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            res=obj.toString();
        }
        return res;
    }


}

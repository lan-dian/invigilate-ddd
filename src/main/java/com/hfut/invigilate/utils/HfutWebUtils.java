package com.hfut.invigilate.utils;

import com.landao.guardian.util.NewxWebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HfutWebUtils {

    /**
     * 套娃(
     */
    public static HttpServletRequest getRequest(){
        return NewxWebUtils.getRequest();
    }

    public static HttpServletResponse getResponse(){
        return NewxWebUtils.getResponse();
    }

    public static void setResponseStatus(HttpStatus status) {
        getResponse().setStatus(status.value());
    }

}

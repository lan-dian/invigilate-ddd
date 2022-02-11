package com.hfut.invigilate.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public abstract class QueryUtils {


    public static QueryWrapper<Object> query(){
        return new QueryWrapper<>();
    }

}

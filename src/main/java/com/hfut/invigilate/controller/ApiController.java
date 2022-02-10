package com.hfut.invigilate.controller;

import com.hfut.invigilate.model.commen.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "公共接口")
@RestController
@RequestMapping("/api")
public class ApiController {

    @ApiOperation("测试")
    @GetMapping("/test")
    public CommonResult<String> test(String name){
        CommonResult<String> result=new CommonResult<>();

        int a=10/0;

        return result.ok("hello"+" "+name);
    }


}

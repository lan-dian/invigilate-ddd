package com.hfut.invigilate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.entity.ExchangeLog;
import com.hfut.invigilate.entity.RequestLog;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.service.ExchangeLogService;
import com.hfut.invigilate.service.RequestLogService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "日志相关")
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    RequestLogService requestLogService;

    @Resource
    ExchangeLogService exchangeLogService;

    @RequiredRole(RoleConst.admin)
    @ApiOperation("请求日志")
    @GetMapping("/request")
    public CommonResult<PageDTO<RequestLog>> getRequestLog(@RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "15") Integer limit){
        CommonResult<PageDTO<RequestLog>> result=new CommonResult<>();

        IPage<RequestLog> iPage=new Page<>(page,limit);

        requestLogService.lambdaQuery()
                .orderByDesc(RequestLog::getCreateTime)
                .page(iPage);

        return result.body(PageDTO.build(iPage));
    }

    @RequiredRole({RoleConst.admin,RoleConst.manager})
    @ApiOperation("交换日志")
    @GetMapping("/exchange")
    public CommonResult<PageDTO<ExchangeLog>> getExchangeLog(@RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "15") Integer limit){
        CommonResult<PageDTO<ExchangeLog>> result=new CommonResult<>();

        IPage<RequestLog> iPage=new Page<>(page,limit);

        requestLogService.lambdaQuery()
                .orderByDesc(RequestLog::getCreateTime)
                .page(iPage);

        return result.body(PageDTO.build(iPage));
    }






}

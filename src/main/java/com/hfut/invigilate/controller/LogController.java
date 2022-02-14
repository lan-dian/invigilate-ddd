package com.hfut.invigilate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.entity.ExchangeLog;
import com.hfut.invigilate.entity.RequestLog;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exchange.ExchangeLogPageQueryDTO;
import com.hfut.invigilate.model.exchange.ExchangeState;
import com.hfut.invigilate.model.exchange.ExchangeType;
import com.hfut.invigilate.service.ExchangeLogService;
import com.hfut.invigilate.service.RequestLogService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
                                                           @RequestParam(defaultValue = "15") Integer limit) {
        CommonResult<PageDTO<RequestLog>> result = new CommonResult<>();

        IPage<RequestLog> iPage = new Page<>(page, limit);

        requestLogService.lambdaQuery()
                .orderByDesc(RequestLog::getCreateTime)
                .page(iPage);

        return result.body(PageDTO.build(iPage));
    }


    @RequiredRole({RoleConst.admin, RoleConst.manager})
    @ApiOperation("交换链路追踪!")
    @PostMapping("/exchange")
    public CommonResult<PageDTO<ExchangeLog>> getExchangeLog(@RequestParam(defaultValue = "1") Integer page,
                                                             @RequestParam(defaultValue = "15") Integer limit,
                                                             @RequestBody ExchangeLogPageQueryDTO queryDTO) {
        CommonResult<PageDTO<ExchangeLog>> result = new CommonResult<>();

        IPage<ExchangeLog> iPage = new Page<>(page, limit);

        Integer ordinal = null;
        ExchangeType exchangeType = queryDTO.getExchangeType();
        if (exchangeType != null) {
            ordinal = exchangeType.ordinal();
        }

        exchangeLogService.lambdaQuery()
                .eq(queryDTO.getWorkId() != null, ExchangeLog::getWorkId, queryDTO.getWorkId())
                .eq(queryDTO.getOnlyShowEssential() == Boolean.TRUE, ExchangeLog::getState, ExchangeState.Result.ordinal())
                .eq(ordinal != null, ExchangeLog::getExchangeType, ordinal)
                .and(queryDTO.getExamCode() != null, query -> query.eq(ExchangeLog::getRequestExamCode, queryDTO.getExamCode())
                        .or()
                        .eq(ExchangeLog::getResponseExamCode, queryDTO.getExamCode()))
                .orderByDesc(ExchangeLog::getCreateTime)
                .page(iPage);

        return result.body(PageDTO.build(iPage));
    }


}

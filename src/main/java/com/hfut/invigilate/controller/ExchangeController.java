package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.business.ExchangeCoreService;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.exchange.ExchangeState;
import com.hfut.invigilate.service.ExchangeService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags="交换相关的动作(数据都在teacher相关那里)")
@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Resource
    ExchangeCoreService exchangeCoreService;

    @Resource
    UserAuthorService userAuthorService;

    @Resource
    ExchangeService exchangeService;

    @RequiredRole(RoleConst.teacher)
    @GetMapping("/start")
    @ApiOperation("发起调换申请")
    public CommonResult<Void> start(@RequestParam Long invigilateCode, @RequestParam String msg) {
        CommonResult<Void> result=new CommonResult<>();
        Integer workId = userAuthorService.getUserId();

        boolean exchange = exchangeCoreService.startExchange(invigilateCode, msg, workId);

        return result.ok(exchange);
    }

    @GetMapping("/replace")
    @ApiOperation("直接顶替别人的监考")
    public CommonResult<Void> replace(@RequestParam Long invigilateCode) {
        CommonResult<Void> result=new CommonResult<>();
        Integer workId = userAuthorService.getUserId();

        boolean replace = exchangeCoreService.replace(workId, invigilateCode);
        return result.ok(replace);
    }


    @GetMapping("/intent")
    @ApiOperation(value = "`想`和别人交换监考")
    public CommonResult<Void> intent(@RequestParam Long invigilateCode, @RequestParam Long targetCode) {
        CommonResult<Void> result=new CommonResult<>();

        Integer workId = userAuthorService.getUserId();

        boolean exchange=exchangeCoreService.intend(workId, invigilateCode, targetCode);

        return result.ok(exchange);
    }

    @GetMapping("/cancel_intend")
    @ApiOperation("取消我和别人的交换意图")
    public CommonResult<Void> cancelIntent(@RequestParam Long exchangeCode){
        CommonResult<Void> result=new CommonResult<>();

        Integer workId = userAuthorService.getUserId();

        boolean cancel = exchangeCoreService.cancelIntend(workId, exchangeCode);
        return result.ok(cancel);
    }

    @GetMapping("/cancel_exchange")
    @ApiOperation("取消我发起的调换")
    public CommonResult<Void> cancelExchange(@RequestParam Long invigilateCode){
        CommonResult<Void> result=new CommonResult<>();

        Integer workId = userAuthorService.getUserId();

        boolean cancel = exchangeCoreService.cancelExchange(workId,invigilateCode);
        return result.ok(cancel);
    }

/*
    @GetMapping("/confirm")
    @ApiOperation("确认和哪一个进行交换")
    public CommonResult confirm(@RequestParam Long exchangeCode) {
        String workId = userAuthorService.getUserId();


        ServiceDTO confirm = invigilateService.confirm(exchangeCode, workId);
        return CommonResult.ok(confirm);
    }



*/

}

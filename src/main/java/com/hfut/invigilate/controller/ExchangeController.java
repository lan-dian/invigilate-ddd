package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.business.ExchangeCoreService;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.service.ExchangeService;
import com.hfut.invigilate.service.InvigilateService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags="交换相关的动作(数据都在teacher相关那里)")
@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Resource
    ExchangeCoreService exchangeCoreService;

    @Resource
    UserAuthorService userAuthorService;

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

    /*@GetMapping("/intent")
    @ApiOperation("要和别人交换多个或一个监考")
    public CommonResult intent( @RequestParam Long[] invigilateCodes, @RequestParam Long targetCode) {
        String workId = userAuthorService.getUserId();


        if(invigilateCodes==null || invigilateCodes.length==0){
            LogUtil.err("ExchangeController_intent","没有选择要调换的监考",new Object[]{workId,invigilateCodes,targetCode});
            return CommonResult.err("请选择想要调换的监考");
        }

        for (Long code : invigilateCodes) {
            invigilateService.exchange(workId, code, targetCode);
        }
        return CommonResult.ok();
    }

    @GetMapping("/confirm")
    @ApiOperation("确认和哪一个进行交换")
    public CommonResult confirm(@RequestParam Long exchangeCode) {
        String workId = userAuthorService.getUserId();


        ServiceDTO confirm = invigilateService.confirm(exchangeCode, workId);
        return CommonResult.ok(confirm);
    }

    @GetMapping("/cancel")
    @ApiOperation("取消调换意图")
    public CommonResult cancel(@RequestParam Long invigilateCode){
        String workId = userAuthorService.getUserId();


        boolean cancel = exchangeService.cancel(workId,invigilateCode);
        return CommonResult.ok(cancel);
    }

    @GetMapping("/my-intend")
    @ApiOperation("列出我的所有交换意图")
    public CommonResult myIntend(){
        String workId = userAuthorService.getUserId();

        List<ExchangeInfoDTO> exchangeInfoDTOS = exchangeService.listMyIntend(workId);
        return CommonResult.body(exchangeInfoDTOS);
    }*/

}

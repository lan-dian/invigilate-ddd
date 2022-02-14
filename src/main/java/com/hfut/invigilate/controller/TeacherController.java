package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.consts.DatePattern;
import com.hfut.invigilate.model.exam.ExamTeachersVO;
import com.hfut.invigilate.model.exchange.IntendVO;
import com.hfut.invigilate.model.exchange.InvigilateExchangeVO;
import com.hfut.invigilate.model.exchange.SelfExchangeIntendVO;
import com.hfut.invigilate.model.exchange.WantToBeExchangeInvigilate;
import com.hfut.invigilate.model.invigilate.TeacherInvigilateVO;
import com.hfut.invigilate.model.user.UserInfoVO;
import com.hfut.invigilate.service.ExamService;
import com.hfut.invigilate.service.ExchangeService;
import com.hfut.invigilate.service.InvigilateService;
import com.hfut.invigilate.service.UserService;
import com.landao.checker.annotations.Check;
import com.landao.guardian.annotations.author.RequiredLogin;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "老师个人或个人与监考相关")
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    UserAuthorService userAuthorService;

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExchangeService exchangeService;

    @Resource
    ExamService examService;

    @Resource
    UserService userService;


    @RequiredRole(RoleConst.teacher)
    @PostMapping("/v2/invigilate/")
    @ApiOperation(value = "获取监考")
    public CommonResult<List<TeacherInvigilateVO>> getMyInvigilate(@RequestParam(required = false) @DateTimeFormat(pattern = DatePattern.DATE) LocalDate startDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DatePattern.DATE) LocalDate endDate) {
        CommonResult<List<TeacherInvigilateVO>> result = new CommonResult<>();

        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                return result.err("开始时间不能晚于结束时间");
            }
        }

        Integer workId = userAuthorService.getUserId();

        List<TeacherInvigilateVO> invigilate = invigilateService.listInvigilate(workId, startDate, endDate);
        return result.body(invigilate);

    }

    @RequiredRole(RoleConst.teacher)
    @GetMapping("/my")
    @ApiOperation("查看我发起的调换申请")
    public CommonResult<List<WantToBeExchangeInvigilate>> listMyExchange() {
        CommonResult<List<WantToBeExchangeInvigilate>> result = new CommonResult<>();
        Integer workId = userAuthorService.getUserId();

        List<WantToBeExchangeInvigilate> wantToBeExchangeInvigilates = invigilateService.listWantToBeExchangeInvigilate(workId);
        return result.body(wantToBeExchangeInvigilates);
    }

    @RequiredLogin
    @GetMapping("/exam")
    @ApiOperation("得到考试的详细信息")
    public CommonResult<ExamTeachersVO> getExam(@RequestParam Long examCode) {
        CommonResult<ExamTeachersVO> result = new CommonResult<>();

        ExamTeachersVO examInfo = examService.getExamTeachersVO(examCode);
        if (examInfo == null) {
            return result.err("考试不存在");
        }

        return result.body(examInfo);
    }

    @RequiredLogin
    @PostMapping("/password")
    @ApiOperation(value = "修改密码", notes = "首次登陆修改密码,不用传递初始密码,之后修改需要传递")
    public CommonResult<Void> changePassword(@RequestParam(defaultValue = "123456")
                                             @Check(name = "旧密码", max = 32)
                                                     String password,
                                             @RequestParam
                                             @Check(name = "新密码", max = 32)
                                                     String newPassword) {
        CommonResult<Void> result = new CommonResult<>();

        if ("123456".equals(newPassword)) {
            return result.err("新密码不能为初始密码");
        }
        if (newPassword.equals(password)) {
            return result.err("新密码和原始密码不能相同");
        }

        boolean change = userAuthorService.changePassword(password, newPassword);

        return result.ok(change,"原密码错误");
    }

    @RequiredLogin
    @GetMapping("/info")
    @ApiOperation("查看自己的个人信息")
    public CommonResult<UserInfoVO> info(){
        CommonResult<UserInfoVO> result=new CommonResult<>();

        Integer workId = userAuthorService.getUserId();

        UserInfoVO userInfo=userService.getUserInfo(workId);

        return result.body(userInfo);
    }

    @RequiredRole(RoleConst.teacher)
    @GetMapping("/my_intend")
    @ApiOperation("列出我的所有交换意图")
    public CommonResult<List<SelfExchangeIntendVO>> myIntend(){
        CommonResult<List<SelfExchangeIntendVO>> result=new CommonResult<>();
        Integer workId = userAuthorService.getUserId();

        List<SelfExchangeIntendVO> exchangeInfoDTOS = exchangeService.listMyIntend(workId);
        return result.body(exchangeInfoDTOS);
    }

    @RequiredRole(RoleConst.teacher)
    @GetMapping("/list_intend")
    @ApiOperation("查看待确认调换的信息(从这里选一个确认和谁调换)")
    public CommonResult<List<IntendVO>> listIntend(@RequestParam Long invigilateCode){
        CommonResult<List<IntendVO>> result=new CommonResult<>();
        List<IntendVO> intendVOS = exchangeService.listOtherIntend(invigilateCode);

        return result.body(intendVOS);
    }


    @GetMapping("/list")
    @ApiOperation("列出其他人的调换申请")
    public CommonResult<List<InvigilateExchangeVO>> listInvigilateExchange(@RequestParam(required = false) @DateTimeFormat(pattern = DatePattern.DATE) LocalDate startDate,
                                               @RequestParam(required = false)  @DateTimeFormat(pattern = DatePattern.DATE) LocalDate endDate) {
        CommonResult<List<InvigilateExchangeVO>> result=new CommonResult<>();
        Integer workId = userAuthorService.getUserId();

        if(startDate!=null){
            if(startDate.isBefore(LocalDate.now())){
                return result.err("开始时间不能早于今天");
            }
            if(endDate!=null){
                if(startDate.isAfter(endDate)){
                    return result.err("开始时间必须晚于结束时间");
                }
            }
        }else{
            startDate=LocalDate.now();
        }

        List<InvigilateExchangeVO> invigilateExchangeVOS = exchangeService.listInvigilateExchanges(workId,startDate,endDate);
        return result.body(invigilateExchangeVOS);
    }

}
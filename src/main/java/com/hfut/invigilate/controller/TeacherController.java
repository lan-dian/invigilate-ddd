package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.consts.DatePattern;
import com.hfut.invigilate.model.invigilate.TeacherInvigilateVO;
import com.hfut.invigilate.service.IInvigilateService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    IInvigilateService iInvigilateService;


    @RequiredRole(RoleConst.teacher)
    @PostMapping("/v2/invigilate/")
    @ApiOperation(value = "获取监考")
    public CommonResult<List<TeacherInvigilateVO>> getMyInvigilate(@RequestParam(required = false) @DateTimeFormat(pattern = DatePattern.DATE) LocalDate startDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DatePattern.DATE) LocalDate endDate){
        CommonResult<List<TeacherInvigilateVO>> result=new CommonResult<>();

        if (startDate!=null && endDate!=null){
            if(endDate.isBefore(startDate)){
                return result.err("开始时间不能晚于结束时间");
            }
        }

        Integer workId = userAuthorService.getUserId();

        List<TeacherInvigilateVO> invigilate = iInvigilateService.listInvigilate(workId, startDate, endDate);
        return result.body(invigilate);

    }

}
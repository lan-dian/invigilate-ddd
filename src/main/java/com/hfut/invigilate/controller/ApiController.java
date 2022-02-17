package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.user.LoginInfoVO;
import com.hfut.invigilate.test.BeanPost;
import com.landao.checker.utils.CheckUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Locale;

@Api(tags = "公共接口")
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    UserAuthorService userAuthorService;

    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public CommonResult<LoginInfoVO> login(@RequestParam Integer workId, @RequestParam String password) {
        CommonResult<LoginInfoVO> result=new CommonResult<>();

        if(CheckUtils.countLength(workId)!=10){
            return result.err("工号位数不合法");
        }

        if(!StringUtils.hasText(password)){
            return result.err("请输入密码");
        }

        password= DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase(Locale.ROOT);

        LoginInfoVO login = userAuthorService.login(workId, password);
        return result.body(login);
    }




}

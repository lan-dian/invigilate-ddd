package com.hfut.invigilate;

import com.hfut.invigilate.controller.ApiController;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.user.LoginInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

@SpringBootTest
public class SpringTest {


    @Autowired
    ApiController apiController;

    @Test
    public void test(){
        CommonResult<LoginInfoVO> login = apiController.login(2020217944, "123456");
        System.out.println(login);
    }

}

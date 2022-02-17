package com.hfut.invigilate.controller;

import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.consts.DatePattern;
import com.hfut.invigilate.model.user.UserDepartmentVO;
import com.hfut.invigilate.model.user.UserPageQueryDTO;
import com.hfut.invigilate.model.user.UserRolesVO;
import com.hfut.invigilate.service.UserService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

    @Resource
    UserAuthorService userAuthorService;

    @Resource
    UserService UserService;


    @RequiredRole(RoleConst.manager)
    @ApiOperation(value = "获取部门成员",notes = "startDate不传递,默认统计一个月的监考次数")
    @GetMapping("/department")
    public CommonResult<List<UserDepartmentVO>> getDepartmentUser(@RequestParam(required = false)
                                                                  @DateTimeFormat(pattern = DatePattern.DATE)
                                                                  LocalDate startDate){
        CommonResult<List<UserDepartmentVO>> result=new CommonResult<>();

        Integer departmentId = userAuthorService.getDepartmentId();
        if(startDate==null){
            startDate=LocalDate.now().minusMonths(1);
        }

        List<UserDepartmentVO> departmentUsers = UserService.getDepartmentUser(departmentId, startDate);

        return result.body(departmentUsers);
    }

    @RequiredRole(value ={ RoleConst.manager,RoleConst.admin})
    @PostMapping("/page")
    @ApiOperation("分页查询")
    public CommonResult<PageDTO<UserRolesVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "15") Integer limit,
                                                   @RequestBody(required = false) UserPageQueryDTO queryDTO){
        CommonResult<PageDTO<UserRolesVO>> result=new CommonResult<>();
        PageDTO<UserRolesVO> pageDTO = UserService.page(page, limit, queryDTO);
        return result.body(pageDTO);
    }


}

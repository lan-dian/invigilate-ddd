package com.hfut.invigilate.author;

import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.user.LoginInfoVO;
import com.hfut.invigilate.model.user.RoleEnum;
import com.hfut.invigilate.service.UserRoleService;
import com.hfut.invigilate.service.UserService;
import com.landao.guardian.annotations.system.GuardianService;
import com.landao.guardian.core.TokenService;
import com.landao.guardian.util.GuardianUtils;

import javax.annotation.Resource;
import java.util.Set;

@GuardianService
public class UserAuthorService extends TokenService<UserTokenBean,Integer> {

    @Resource
    UserService userService;

    @Resource
    UserRoleService userRoleService;

    @Override
    public Set<String> getRoles() {
        Set<RoleEnum> roles = userRoleService.getRoles(getUserId());
        return GuardianUtils.enumToString(roles);
    }

    public Integer getDepartmentId(){
        User user = userService.lambdaQuery()
                .eq(User::getWorkId, getUserId()).one();
        return user.getDepartmentId();
    }

    public LoginInfoVO login(Integer workId, String password) {
        User user = userService.lambdaQuery().eq(User::getWorkId, workId).one();
        if(user==null){
            throw new BusinessException("用户不存在");
        }

        if(!user.checkPassword(password)){
            throw new BusinessException("密码错误");
        }

        Set<RoleEnum> roles = userRoleService.getRoles(workId);

        UserTokenBean userTokenBean = UserTokenBean.convert(user);

        String token = parseToken(userTokenBean);

        return new LoginInfoVO(token,roles,user.getName().substring(0,1));
    }


}

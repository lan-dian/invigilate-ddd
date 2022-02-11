package com.hfut.invigilate.author;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.entity.UserRole;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.user.LoginInfoVO;
import com.hfut.invigilate.model.user.RoleEnum;
import com.hfut.invigilate.service.IUserRoleService;
import com.hfut.invigilate.service.IUserService;
import com.landao.guardian.annotations.system.GuardianService;
import com.landao.guardian.core.TokenService;
import com.landao.guardian.util.GuardianUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

@GuardianService
public class UserAuthorService extends TokenService<UserTokenBean,Integer> {

    @Resource
    IUserService iUserService;

    @Resource
    IUserRoleService iUserRoleService;

    @Override
    public Set<String> getRoles() {
        Set<RoleEnum> roles = iUserRoleService.getRoles(getUserId());
        return GuardianUtils.enumToString(roles);
    }

    public Integer getDepartmentId(){
        User user = iUserService.lambdaQuery()
                .eq(User::getWorkId, getUserId()).one();
        return user.getDepartmentId();
    }

    public LoginInfoVO login(Integer workId, String password) {
        User user = iUserService.lambdaQuery().eq(User::getWorkId, workId).one();
        if(user==null){
            throw new BusinessException("用户不存在");
        }

        if(!user.checkPassword(password)){
            throw new BusinessException("密码错误");
        }

        Set<RoleEnum> roles = iUserRoleService.getRoles(workId);

        UserTokenBean userTokenBean = UserTokenBean.convert(user);

        String token = parseToken(userTokenBean);

        return new LoginInfoVO(token,roles,user.getName().substring(0,1));
    }


}

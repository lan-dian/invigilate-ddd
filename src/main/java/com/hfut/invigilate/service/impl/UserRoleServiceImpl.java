package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.UserRole;
import com.hfut.invigilate.mapper.UserRoleMapper;
import com.hfut.invigilate.model.user.RoleEnum;
import com.hfut.invigilate.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public Set<RoleEnum> getRoles(Integer workId){
        List<UserRole> userRoles = lambdaQuery().eq(UserRole::getWorkId, workId).list();

        return userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
    }


}

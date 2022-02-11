package com.hfut.invigilate.service;

import com.hfut.invigilate.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hfut.invigilate.model.user.RoleEnum;

import java.util.Set;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface IUserRoleService extends IService<UserRole> {

    Set<RoleEnum> getRoles(Integer workId);
}

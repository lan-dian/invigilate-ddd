package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.entity.UserRole;
import com.hfut.invigilate.mapper.UserMapper;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.user.*;
import com.hfut.invigilate.service.DepartmentService;
import com.hfut.invigilate.service.UserRoleService;
import com.hfut.invigilate.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserRoleService userRoleService;


    @Override
    public PageDTO<UserRolesVO> page(Integer page,Integer limit,UserPageQueryDTO query){
        List<UserRolesVO> userRolesVOS = baseMapper.page((page - 1) * limit, limit, query);
        Long count = baseMapper.count(query);
        return PageDTO.build(count,userRolesVOS);
    }

    @Override
    @Transactional
    public boolean update(UserAdminVO userAdminVO){
        Integer workId = userAdminVO.getWorkId();
        User user = lambdaQuery()
                .select(User::getWorkId,User::getId)
                .eq(User::getWorkId,workId).one();
        if(user==null){
            throw new BusinessException("工号不存在");
        }

        Integer departmentId = userAdminVO.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        if(department==null){
            throw new BusinessException("部门id不存在");
        }

        user.update(userAdminVO);//更新为userAdmin的信息
        boolean updateUserInfo = updateById(user);
        if(!updateUserInfo){
            throw new BusinessException("用户信息更新失败");
        }

        //角色更新算法
        Set<RoleEnum> oldRoles = userRoleService.getRoles(workId);//N个
        Set<RoleEnum> newRoles = userAdminVO.getRoles();//M个
        Iterator<RoleEnum> oldRolesIterator = oldRoles.iterator();
        while (oldRolesIterator.hasNext()){//删除重复 N
            RoleEnum oldRole = oldRolesIterator.next();
            if(newRoles.contains(oldRole)){
                oldRolesIterator.remove();
                newRoles.remove(oldRole);
            }
        }
        oldRolesIterator=oldRoles.iterator();
        Iterator<RoleEnum> newRolesIterator = newRoles.iterator();
        while (oldRolesIterator.hasNext() && newRolesIterator.hasNext()){//两个都不为空,更新
            RoleEnum oldRole = oldRolesIterator.next();
            RoleEnum newRole = newRolesIterator.next();
            boolean update = userRoleService.lambdaUpdate()
                    .set(UserRole::getRole, newRole.ordinal())
                    .eq(UserRole::getWorkId, workId)
                    .eq(UserRole::getRole, oldRole.ordinal())
                    .update();
            if(!update){
                throw new BusinessException("角色更新失败");
            }
        }
        while (oldRolesIterator.hasNext()){//删除
            RoleEnum oldRole = oldRolesIterator.next();
            boolean remove = userRoleService.lambdaUpdate()
                    .eq(UserRole::getWorkId, workId)
                    .eq(UserRole::getRole, oldRole.ordinal())
                    .remove();
            if(!remove){
                throw new BusinessException("角色删除失败");
            }
        }
        while (newRolesIterator.hasNext()){//添加
            RoleEnum newRole = newRolesIterator.next();
            UserRole userRole = new UserRole();
            userRole.setWorkId(workId);
            userRole.setRole(newRole);
            boolean save = userRoleService.save(userRole);
            if(!save){
                throw new BusinessException("角色保存失败");
            }
        }
        return true;
    }


    @Override
    public List<UserDepartmentVO> getDepartmentUser(Integer departmentId, LocalDate startDate) {
        return baseMapper.listUserDepartmentVO(departmentId, startDate);
    }

    @Override
    public UserInfoVO getUserInfo(Integer workId) {
        return baseMapper.getUserInfo(workId);
    }


}

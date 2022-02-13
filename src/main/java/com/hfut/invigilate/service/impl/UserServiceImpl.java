package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.mapper.UserMapper;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.user.UserDepartmentVO;
import com.hfut.invigilate.model.user.UserInfoVO;
import com.hfut.invigilate.model.user.UserPageQueryDTO;
import com.hfut.invigilate.model.user.UserRolesVO;
import com.hfut.invigilate.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public PageDTO<UserRolesVO> page(Integer page,Integer limit,UserPageQueryDTO query){
        List<UserRolesVO> userRolesVOS = baseMapper.page((page - 1) * limit, limit, query);
        Long count = baseMapper.count(query);
        return PageDTO.build(count,userRolesVOS);
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

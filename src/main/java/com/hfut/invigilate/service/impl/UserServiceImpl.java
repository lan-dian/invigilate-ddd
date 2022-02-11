package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.mapper.UserMapper;
import com.hfut.invigilate.model.user.UserDepartmentVO;
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
    public List<UserDepartmentVO> getDepartmentUser(Integer departmentId, LocalDate startDate) {
        return baseMapper.listUserDepartmentVO(departmentId, startDate);
    }

}

package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.mapper.UserMapper;
import com.hfut.invigilate.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

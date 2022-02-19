package com.hfut.invigilate.service;

import com.hfut.invigilate.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.user.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface UserService extends IService<User> {

    PageDTO<UserRolesVO> page(Integer page, Integer limit, UserPageQueryDTO query);

    boolean update(UserAdminVO userAdminVO);

    List<UserDepartmentVO> getDepartmentUser(Integer departmentId, LocalDate startDate);

    UserInfoVO getUserInfo(Integer workId);
}

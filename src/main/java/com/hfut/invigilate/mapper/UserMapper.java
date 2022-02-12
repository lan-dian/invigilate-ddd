package com.hfut.invigilate.mapper;

import com.hfut.invigilate.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.model.user.UserDepartmentVO;
import com.hfut.invigilate.model.user.UserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface UserMapper extends BaseMapper<User> {

    List<UserDepartmentVO> listUserDepartmentVO(@Param("departmentId") Integer departmentId, @Param("startDate") LocalDate startDate);

    UserInfoVO getUserInfo(@Param("workId") Integer workId);

}

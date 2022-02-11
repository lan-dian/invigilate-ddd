package com.hfut.invigilate.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hfut.invigilate.entity.Invigilate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.model.invigilate.InvigilateBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 监考 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface InvigilateMapper extends BaseMapper<Invigilate> {

    List<InvigilateBO> listInvigilate(@Param(Constants.WRAPPER) QueryWrapper<Object> query);

}

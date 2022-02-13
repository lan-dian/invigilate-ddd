package com.hfut.invigilate.mapper;

import com.hfut.invigilate.entity.Exchange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.model.exchange.SelfExchangeIntendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 交换记录 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface ExchangeMapper extends BaseMapper<Exchange> {

    List<SelfExchangeIntendVO> listTeacherIntend(@Param("workId") Integer workId);

}

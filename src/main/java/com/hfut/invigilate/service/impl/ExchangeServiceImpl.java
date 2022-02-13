package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Exchange;
import com.hfut.invigilate.mapper.ExchangeMapper;
import com.hfut.invigilate.model.exchange.IntendVO;
import com.hfut.invigilate.model.exchange.SelfExchangeIntendVO;
import com.hfut.invigilate.service.ExchangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 交换记录 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class ExchangeServiceImpl extends ServiceImpl<ExchangeMapper, Exchange> implements ExchangeService {

    @Override
    public List<SelfExchangeIntendVO> listMyIntend(Integer workId) {
        return baseMapper.listTeacherIntend(workId);
    }

    @Override
    public List<IntendVO> listOtherIntend(Long invigilateCode) {
        return baseMapper.listOtherIntend(invigilateCode);
    }
}

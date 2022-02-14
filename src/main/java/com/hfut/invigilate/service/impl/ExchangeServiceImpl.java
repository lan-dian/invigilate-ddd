package com.hfut.invigilate.service.impl;

import com.hfut.invigilate.entity.Exchange;
import com.hfut.invigilate.mapper.ExchangeMapper;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.exchange.ExchangeState;
import com.hfut.invigilate.model.exchange.IntendVO;
import com.hfut.invigilate.model.exchange.InvigilateExchangeVO;
import com.hfut.invigilate.model.exchange.SelfExchangeIntendVO;
import com.hfut.invigilate.service.ExchangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

/*    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean clearPossibleExchange(Long invigilateCode){
        List<Exchange> requestExchanges = lambdaQuery()
                .eq(Exchange::getState, ExchangeState.Process.ordinal())//交换成功的不能管
                .and(q -> q.eq(Exchange::getResponseInvigilateCode, invigilateCode)
                        .or().eq(Exchange::getRequestInvigilateCode, invigilateCode)).list();
        if(!requestExchanges.isEmpty()){
            //发送通知
            List<Long> exchangesIds = requestExchanges.stream().map(Exchange::getCode).collect(Collectors.toList());
            boolean remove = removeByIds(exchangesIds);
            if(!remove){
                throw new BusinessException("相关交换记录清除失败");
            }
        }
        return true;
    }*/

    @Override
    public Exchange getByCode(Long code){
        return lambdaQuery().eq(Exchange::getCode, code).one();
    }

    @Override
    public List<SelfExchangeIntendVO> listMyIntend(Integer workId) {
        return baseMapper.listTeacherIntend(workId);
    }

    @Override
    public List<IntendVO> listOtherIntend(Long invigilateCode) {
        return baseMapper.listOtherIntend(invigilateCode);
    }

    @Override
    public List<InvigilateExchangeVO> listInvigilateExchanges(Integer workId, LocalDate startDate, LocalDate endDate) {
        return baseMapper.listInvigilateExchanges(workId, startDate, endDate);
    }


}

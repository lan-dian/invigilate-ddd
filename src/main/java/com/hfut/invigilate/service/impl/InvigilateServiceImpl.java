package com.hfut.invigilate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfut.invigilate.entity.Invigilate;
import com.hfut.invigilate.mapper.InvigilateMapper;
import com.hfut.invigilate.model.exchange.WantToBeExchangeInvigilate;
import com.hfut.invigilate.model.invigilate.InvigilateBO;
import com.hfut.invigilate.model.invigilate.TeacherInvigilateVO;
import com.hfut.invigilate.service.InvigilateService;
import com.hfut.invigilate.utils.QueryUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 监考 服务实现类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Service
public class InvigilateServiceImpl extends ServiceImpl<InvigilateMapper, Invigilate> implements InvigilateService {


    @Override
    public boolean addExchangeNum(Long code){
        return baseMapper.addExchangeNum(code) > 0;
    }


    @Override
    public Invigilate getByCode(Long code){
        return lambdaQuery().eq(Invigilate::getCode, code).one();
    }


    @Override
    public List<TeacherInvigilateVO> listInvigilate(Integer workId, LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        if (startDate == null && endDate == null) {
            startDate = now.minusDays(now.getDayOfWeek().getValue());
        }

        QueryWrapper<Object> query = QueryUtils.query().ge(startDate != null, "EI.date", startDate)
                .le(endDate != null, "EI.date", endDate)
                .eq("IR.work_id", workId);

        List<InvigilateBO> invigilates = baseMapper.listInvigilate(query);

        return invigilates.stream().map(E->TeacherInvigilateVO.convert(E,now)).collect(Collectors.toList());
    }

    @Override
    public List<WantToBeExchangeInvigilate> listWantToBeExchangeInvigilate(Integer workId) {
        List<WantToBeExchangeInvigilate> wantToBeExchangeInvigilates = baseMapper.listWantToBeExchangeInvigilate(workId);
        wantToBeExchangeInvigilates.forEach(WantToBeExchangeInvigilate::confirmState);
        return wantToBeExchangeInvigilates;
    }

}

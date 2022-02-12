package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExchangeCoreService;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.entity.ExchangeLog;
import com.hfut.invigilate.entity.Invigilate;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.service.ConfigService;
import com.hfut.invigilate.service.ExamService;
import com.hfut.invigilate.service.ExchangeLogService;
import com.hfut.invigilate.service.InvigilateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExchangeCoreServiceImpl implements ExchangeCoreService {

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExamService examService;

    @Resource
    ConfigService configService;

    @Resource
    ExchangeLogService exchangeLogService;

    @Override
    public boolean startExchange(Long invigilateCode, String msg, Integer workId) {
        //验证监考
        Invigilate invigilate = invigilateService.lambdaQuery()
                .eq(Invigilate::getCode, invigilateCode)
                .eq(Invigilate::getWorkId, workId)
                .one();
        if(invigilate==null){
            throw new BusinessException("监考编码不合法");
        }
        if(invigilate.getExchangeNum()>=0){
            throw new BusinessException("这场监考已经处于待交换状态中了");
        }

        Exam exam = examService.lambdaQuery().eq(Exam::getCode, invigilate.getExamCode()).one();

        Integer minute = configService.getInteger(ConfigConst.forbidExchange);

        exam.startExchangeAble(minute);

        //设置监考状态
        boolean update = invigilateService.lambdaUpdate()
                .set(Invigilate::getExchangeNum, 0)
                .set(Invigilate::getMsg, msg)
                .eq(Invigilate::getCode, invigilate.getCode())
                .update();
        if(update){
            //记录交换状态
            ExchangeLog exchangeLog = new ExchangeLog();
            exchangeLog.setWorkId(workId);
            exchangeLog.setRequestExamCode(exam.getCode());
            exchangeLog.setResponseExamCode(null);
            exchangeLog.setDetail(exam.getSimpleDescription()+"\t交换期望:"+msg);
            exchangeLog.setState(0);
            exchangeLogService.save(exchangeLog);
        }


        return update;
    }

    @Override
    public boolean replace(Integer workId, Long invigilateCode) {
        return false;
    }

}

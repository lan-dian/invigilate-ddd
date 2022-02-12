package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.business.ExchangeCoreService;
import com.hfut.invigilate.entity.Exam;
import com.hfut.invigilate.entity.Exchange;
import com.hfut.invigilate.entity.ExchangeLog;
import com.hfut.invigilate.entity.Invigilate;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.exchange.ExchangeType;
import com.hfut.invigilate.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeCoreServiceImpl implements ExchangeCoreService {

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExamService examService;

    @Resource
    ConfigService configService;

    @Resource
    ExchangeService exchangeService;

    @Resource
    ExchangeLogService exchangeLogService;

    @Override
    public boolean startExchange(Long invigilateCode, String msg, Integer workId) {
        //验证监考
        Invigilate invigilate = invigilateService.lambdaQuery()
                .eq(Invigilate::getCode, invigilateCode)
                .eq(Invigilate::getWorkId, workId)
                .one();
        if (invigilate == null) {
            throw new BusinessException("监考编码不合法");
        }
        if (invigilate.getExchangeNum() >= 0) {
            throw new BusinessException("这场监考已经处于待交换状态中了");
        }

        Exam exam = examService.getByCode(invigilate.getExamCode());

        Integer minute = configService.getInteger(ConfigConst.forbidExchange);

        exam.startExchangeAble(minute);

        //设置监考状态
        boolean update = invigilateService.lambdaUpdate()
                .set(Invigilate::getExchangeNum, 0)
                .set(Invigilate::getMsg, msg)
                .eq(Invigilate::getCode, invigilate.getCode())
                .update();
        if (update) {
            //记录交换状态
            ExchangeLog exchangeLog = new ExchangeLog();
            exchangeLog.setWorkId(workId);
            exchangeLog.setRequestExamCode(exam.getCode());
            exchangeLog.setResponseExamCode(null);
            exchangeLog.setDetail(exam.getSimpleDescription() + "\t交换期望:" + msg);
            exchangeLog.setState(0);
            exchangeLog.setExchangeType(ExchangeType.Start);
            exchangeLogService.save(exchangeLog);
        }

        return update;
    }

    @Override
    @Transactional
    public boolean replace(Integer workId, Long invigilateCode) {
        Invigilate invigilate = invigilateService.getByCode(invigilateCode);
        if (invigilate == null) {
            throw new BusinessException("监考编码不合法");
        }
        if (invigilate.getWorkId().equals(workId)) {
            throw new BusinessException("你不能顶替自己的监考");
        }
        if (invigilate.getExchangeNum() == -1) {
            throw new BusinessException("该监考未处于调换状态");
        }
        Long examCode = invigilate.getExamCode();

        Exam exam = examService.getByCode(examCode);
        //时间是否过期
        exam.replaceAble();
        //和自己的考试是否有冲突

        List<Exam> conflictExams = examService.listConflictExam(exam, workId);
        if (!conflictExams.isEmpty()) {
            StringBuilder sb = new StringBuilder("这场考试和您的以下考试有时间冲突:\n");
            for (Exam conflictExam : conflictExams) {
                sb.append(conflictExam.getSimpleDescription()).append("\n");
            }
            throw new BusinessException(sb.toString());
        }

        //开始交换
        boolean update = invigilateService.lambdaUpdate()
                .set(Invigilate::getWorkId, workId)
                .set(Invigilate::getExchangeNum, -1)
                .set(Invigilate::getMsg, "")
                .eq(Invigilate::getCode, invigilateCode)
                .update();
        if (!update) {
            throw new BusinessException("未知原因交换失败");
        }
        //更新成功
        //查询相关交换信息
        List<Exchange> exchanges = exchangeService.lambdaQuery()
                .eq(Exchange::getState, 0)//交换成功的不能管
                .and(q -> q.eq(Exchange::getResponseInvigilateCode, invigilateCode)
                        .or().eq(Exchange::getRequestExamCode, invigilateCode)).list();
        //todo 发送相关信息
        List<Long> ids = exchanges.stream().map(Exchange::getId).collect(Collectors.toList());
        if(!exchanges.isEmpty()){
            boolean remove = exchangeService.removeByIds(ids);
            if(!remove){
                throw new BusinessException("有交换记录删除失败");
            }
        }
        //只剩下记录了
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setWorkId(workId);
        exchangeLog.setRequestExamCode(examCode);
        exchangeLog.setDetail(exam.getSimpleDescription());
        exchangeLog.setState(1);
        exchangeLog.setExchangeType(ExchangeType.Replace);
        exchangeLogService.save(exchangeLog);

        return true;
    }

}

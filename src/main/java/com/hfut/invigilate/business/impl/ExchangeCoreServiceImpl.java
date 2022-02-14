package com.hfut.invigilate.business.impl;

import com.hfut.invigilate.author.UserTokenBean;
import com.hfut.invigilate.business.ExchangeCoreService;
import com.hfut.invigilate.entity.*;
import com.hfut.invigilate.model.consts.ConfigConst;
import com.hfut.invigilate.model.exception.BusinessException;
import com.hfut.invigilate.model.exchange.ExchangeState;
import com.hfut.invigilate.model.exchange.ExchangeType;
import com.hfut.invigilate.model.user.UserInfoVO;
import com.hfut.invigilate.service.*;
import com.hfut.invigilate.utils.CodeUtils;
import com.landao.guardian.core.GuardianContext;
import com.landao.guardian.util.RedisUtils;
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

    @Resource
    UserService userService;

    @Override
    @Transactional
    public boolean cancelIntend(Integer workId, Long exchangeCode) {
        Exchange exchange = exchangeService.lambdaQuery()
                .eq(Exchange::getCode, exchangeCode)
                .eq(Exchange::getResponseWorkId, workId)
                .one();
        if (exchange == null) {
            throw new BusinessException("这不是你的合法交换编码");
        }
        //删除记录
        boolean remove = exchangeService.lambdaUpdate()
                .eq(Exchange::getCode, exchangeCode)
                .remove();
        if (!remove) {
            throw new BusinessException("交换记录删除失败");
        }
        //更新监考表
        Long requestInvigilateCode = exchange.getRequestInvigilateCode();
        boolean subExchangeNum = invigilateService.subExchangeNum(requestInvigilateCode);
        if (!subExchangeNum) {
            throw new BusinessException("监考交换数量减少失败");
        }
        //记录日志
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setWorkId(workId);
        exchangeLog.setRequestExamCode(exchange.getRequestExamCode());
        exchangeLog.setResponseExamCode(exchange.getResponseExamCode());
        Exam responseExam = examService.getByCode(exchange.getResponseExamCode());
        Exam requestExam = examService.getByCode(exchange.getRequestExamCode());
        exchangeLog.setDetail("取消用自己的考试:\t" + responseExam.getSimpleDescription() + "\t和\t" + requestExam.getSimpleDescription() + "\t 进行交换");
        exchangeLog.setState(ExchangeState.Process);
        exchangeLog.setExchangeType(ExchangeType.Cancel);
        exchangeLogService.save(exchangeLog);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelExchange(Integer workId, Long invigilateCode) {
        Invigilate invigilate = invigilateService.lambdaQuery()
                .eq(Invigilate::getWorkId, workId)
                .eq(Invigilate::getCode, invigilateCode)
                .one();
        if (invigilate == null) {
            throw new BusinessException("这不是你的合法监考");
        }
        boolean update = invigilateService.lambdaUpdate()
                .set(Invigilate::getExchangeNum, -1)
                .set(Invigilate::getMsg, "")
                .eq(Invigilate::getCode, invigilateCode)
                .update();
        if (!update) {
            throw new BusinessException("监考信息更新失败");
        }
        //查找所有相关的交换
        List<Exchange> exchanges = exchangeService.lambdaQuery()
                .eq(Exchange::getState, ExchangeState.Process)
                .and(query -> query.eq(Exchange::getRequestInvigilateCode, invigilateCode)
                        .or()
                        .eq(Exchange::getResponseInvigilateCode, invigilateCode))
                .list();
        if (!exchanges.isEmpty()) {
            //发送通知

            //删除
            List<Long> exchangeIds = exchanges.stream()
                    .map(Exchange::getId)
                    .collect(Collectors.toList());
            boolean remove = exchangeService.removeByIds(exchangeIds);
            if (!remove) {
                throw new BusinessException("相关交换记录删除失败");
            }
        }

        //记录日志
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setWorkId(workId);
        exchangeLog.setRequestExamCode(invigilate.getExamCode());
        exchangeLog.setResponseExamCode(null);
        Exam exam = examService.getByCode(invigilate.getExamCode());
        exchangeLog.setDetail("取消了和别人交换考试:\t"+exam.getSimpleDescription());
        exchangeLog.setState(ExchangeState.Process);
        exchangeLog.setExchangeType(ExchangeType.Cancel);
        exchangeLogService.save(exchangeLog);

        return true;
    }

    @Override
    @Transactional
    public boolean confirm(Long exchangeCode, Integer requestWorkId) {
        Exchange exchange = exchangeService.lambdaQuery()
                .eq(Exchange::getCode, exchangeCode)
                .eq(Exchange::getState,ExchangeState.Process.ordinal())
                .one();
        if(exchange==null){
            throw new BusinessException("这不是一个合法的交换编码");
        }
        if(!exchange.getRequestWorkId().equals(requestWorkId)){
            throw new BusinessException("这场考试不是和你交换的");
        }

        Exam requestExam = examService.getByCode(exchange.getRequestExamCode());
        Exam responseExam = examService.getByCode(exchange.getResponseExamCode());
        //两次考试都不能过期
        if(!requestExam.beforeStart()){
            throw new BusinessException("你的考试已经开始过了");
        }
        if(!responseExam.beforeStart()){
            throw new BusinessException("对方的考试已经开始过了");
        }
        //对放的考试和我的考试除了要交换的，其他都不冲突
        throwIfHasConflictExam(requestExam,requestWorkId);
        //可以交换
        boolean updateRequestInvigilate = invigilateService.lambdaUpdate()
                .set(Invigilate::getWorkId, exchange.getResponseWorkId())
                .set(Invigilate::getExchangeNum, -1)
                .set(Invigilate::getMsg, "")
                .eq(Invigilate::getCode,exchange.getRequestInvigilateCode())
                .update();
        if(!updateRequestInvigilate){
            throw new BusinessException("你的监考记录更新失败");
        }
        boolean updateResponseInvigilate = invigilateService.lambdaUpdate()
                .set(Invigilate::getWorkId, exchange.getRequestWorkId())
                .set(Invigilate::getExchangeNum, -1)
                .set(Invigilate::getMsg, "")
                .eq(Invigilate::getCode, exchange.getResponseInvigilateCode())
                .update();
        if(!updateResponseInvigilate){
            throw new BusinessException("对方监考更新失败");
        }
        //更新交换记录
        boolean update = exchangeService.lambdaUpdate()
                .set(Exchange::getState, ExchangeState.Result.ordinal())
                .eq(Exchange::getCode, exchangeCode)
                .update();
        if(!update){
            throw new BusinessException("交换记录更新失败");
        }

        //删除所有可能的交换记录
        List<Exchange> requestExchanges = exchangeService.lambdaQuery()
                .eq(Exchange::getState, ExchangeState.Process.ordinal())//交换成功的不能管
                .and(q -> q.eq(Exchange::getResponseInvigilateCode, exchange.getRequestInvigilateCode())
                        .or().eq(Exchange::getRequestInvigilateCode, exchange.getRequestInvigilateCode())).list();
        if(!requestExchanges.isEmpty()){
            //发送通知
            List<Long> exchangesIds = requestExchanges.stream().map(Exchange::getCode).collect(Collectors.toList());
            boolean remove = exchangeService.removeByIds(exchangesIds);
            if(!remove){
                throw new BusinessException("相关交换记录清除失败");
            }
        }
        List<Exchange> reponseExchanges = exchangeService.lambdaQuery()
                .eq(Exchange::getState, ExchangeState.Process.ordinal())//交换成功的不能管
                .and(q -> q.eq(Exchange::getResponseInvigilateCode, exchange.getResponseInvigilateCode())
                        .or().eq(Exchange::getRequestInvigilateCode, exchange.getResponseInvigilateCode())).list();
        if(!reponseExchanges.isEmpty()){
            //发送通知
            List<Long> exchangesIds = reponseExchanges.stream().map(Exchange::getCode).collect(Collectors.toList());
            boolean remove = exchangeService.removeByIds(exchangesIds);
            if(!remove){
                throw new BusinessException("相关交换记录清除失败");
            }
        }
        //做日志
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setWorkId(requestWorkId);
        exchangeLog.setRequestExamCode(exchange.getRequestExamCode());
        exchangeLog.setResponseExamCode(exchange.getResponseExamCode());
        UserInfoVO userInfo = userService.getUserInfo(exchange.getResponseWorkId());

        //注意两者的考试所属已经发生了变化
        exchangeLog.setDetail(requestWorkId+" "+ GuardianContext.getUser(UserTokenBean.class).getName()
                +"确认用自己的考试:"+responseExam.getSimpleDescription()+"\t和\t"+exchange.getResponseWorkId()+" "
                +userInfo.getName()+"的考试:"+requestExam.getSimpleDescription()+"进行交换");
        exchangeLog.setState(ExchangeState.Result);
        exchangeLog.setExchangeType(ExchangeType.Confirm);
        boolean save = exchangeLogService.save(exchangeLog);
        if(!save){
            throw new BusinessException("重要日志记录失败");
        }

        return true;
    }

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
            exchangeLog.setState(ExchangeState.Process);
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

        throwIfHasConflictExam(exam,workId);

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
                .eq(Exchange::getState, ExchangeState.Process.ordinal())//交换成功的不能管
                .and(q -> q.eq(Exchange::getResponseInvigilateCode, invigilateCode)
                        .or().eq(Exchange::getRequestInvigilateCode, invigilateCode)).list();
        //todo 发送相关信息
        List<Long> ids = exchanges.stream().map(Exchange::getId).collect(Collectors.toList());
        if (!exchanges.isEmpty()) {
            boolean remove = exchangeService.removeByIds(ids);
            if (!remove) {
                throw new BusinessException("有交换记录删除失败");
            }
        }
        //只剩下记录了
        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setWorkId(workId);
        exchangeLog.setRequestExamCode(examCode);
        exchangeLog.setDetail(exam.getSimpleDescription());
        exchangeLog.setState(ExchangeState.Result);
        exchangeLog.setExchangeType(ExchangeType.Replace);
        boolean save = exchangeLogService.save(exchangeLog);
        if (!save) {
            throw new BusinessException("关键交换日志记录失败!");
        }

        return true;
    }

    @Override
    @Transactional
    public synchronized boolean intend(Integer workId, Long responseInvigilateCode, Long requestInvigilateCode) {
        Invigilate responseInvigilate = invigilateService.lambdaQuery()
                .eq(Invigilate::getWorkId, workId)
                .eq(Invigilate::getCode, responseInvigilateCode)
                .one();
        if (responseInvigilate == null) {
            throw new BusinessException("这不是你的合法的监考");
        }

        Invigilate requestInvigilate = invigilateService.getByCode(requestInvigilateCode);
        if (requestInvigilate == null) {
            throw new BusinessException("目标监考不存在");
        }
        if (requestInvigilate.getWorkId().equals(workId)) {
            throw new BusinessException("你不能和自己交换监考");
        }
        if (requestInvigilate.getExchangeNum() < 0) {
            throw new BusinessException("目标监考不想和别人调换");
        }

        Long requestExamCode = requestInvigilate.getExamCode();
        Exam requestExam = examService.getByCode(requestExamCode);
        if (!requestExam.beforeStart()) {
            throw new BusinessException("这场考试已经开始或者结束了!");
        }
        //看看我的考试过期了吗
        Exam responseExam = examService.getByCode(responseInvigilate.getExamCode());
        if (!responseExam.beforeStart()) {
            throw new BusinessException("你的考试已经开始或者结束了");
        }

        if (responseExam.getCode().equals(requestExamCode)) {
            throw new BusinessException("你们两个在同一个考场");
        }

        //我和他交换过了吗
        Exchange hasExchange = exchangeService.lambdaQuery()
                .eq(Exchange::getState, ExchangeState.Process)
                .eq(Exchange::getRequestInvigilateCode, requestInvigilateCode)
                .eq(Exchange::getResponseInvigilateCode, responseInvigilateCode)
                .one();
        if (hasExchange != null) {
            throw new BusinessException("你已经和他交换过这场考试了");
        }


        //这个考试和我的时间有冲突吗,跳过我这场考试
        throwIfHasConflictExam(requestExam,workId);

        //构建交换记录
        Exchange exchange = new Exchange();
        exchange.setRequestWorkId(requestInvigilate.getWorkId());
        exchange.setRequestExamCode(requestExamCode);
        exchange.setRequestInvigilateCode(requestInvigilateCode);
        exchange.setResponseWorkId(workId);
        exchange.setResponseExamCode(responseExam.getCode());
        exchange.setResponseInvigilateCode(responseInvigilateCode);
        exchange.setState(ExchangeState.Process);
        exchange.setCode(CodeUtils.getId());
        boolean save = exchangeService.save(exchange);
        if (save) {
            boolean addExchangeNum = invigilateService.addExchangeNum(requestInvigilateCode);
            if (!addExchangeNum) {
                throw new BusinessException("交换数量增加失败");
            }
        }

        if (save) {
            //构建交换记录
            ExchangeLog exchangeLog = new ExchangeLog();
            exchangeLog.setWorkId(workId);
            exchangeLog.setRequestExamCode(requestExamCode);
            exchangeLog.setResponseExamCode(responseExam.getCode());
            exchangeLog.setDetail("想要以 " + responseExam.getSimpleDescription() + " 和 " + requestExam.getSimpleDescription() + " 进行交换");
            exchangeLog.setState(ExchangeState.Process);
            exchangeLog.setExchangeType(ExchangeType.Intend);
            exchangeLogService.save(exchangeLog);
        }

        return save;
    }

    private void throwIfHasConflictExam(Exam exam,Integer workId){
        List<Exam> conflictExams = examService.listConflictExam(exam, workId);
        if (!conflictExams.isEmpty()) {
            conflictExams.removeIf(E -> E.getCode().equals(exam.getCode()));
            if (!conflictExams.isEmpty()) {
                StringBuilder sb = new StringBuilder("这场考试和您的以下考试有时间冲突:\n");
                for (Exam conflictExam : conflictExams) {
                    sb.append(conflictExam.getSimpleDescription()).append("\n");
                }
                throw new BusinessException(sb.toString());
            }
        }
    }

}

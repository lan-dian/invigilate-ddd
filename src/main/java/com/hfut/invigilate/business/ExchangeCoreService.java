package com.hfut.invigilate.business;

public interface ExchangeCoreService {

    boolean startExchange(Long invigilateCode, String msg, Integer workId);

    boolean replace(Integer workId, Long invigilateCode);

}

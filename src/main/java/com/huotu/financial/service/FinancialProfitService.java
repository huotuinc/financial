package com.huotu.financial.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface FinancialProfitService {

    /**
     * 每天凌晨1点进行计算收益
     */
    @Scheduled(cron = "0 0 0 * * *")
    void countProfit();

}

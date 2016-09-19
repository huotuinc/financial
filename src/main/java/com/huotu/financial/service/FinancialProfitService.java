package com.huotu.financial.service;

import com.huotu.financial.model.ViewProfitListModel;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface FinancialProfitService {

    /**
     * 每天凌晨1点进行计算收益
     */
    void countProfit();


    List<ViewProfitListModel> profitList(Long userId, Long lastId);
}

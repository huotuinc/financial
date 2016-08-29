package com.huotu.financial.service;

import com.huotu.financial.entity.FinancialBuyFlow;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface FinancialBuyFlowService {

    /**
     * 处理回购
     *
     * @param financialBuyFlow
     */
    void handleRedeem(FinancialBuyFlow financialBuyFlow) throws Exception;
}

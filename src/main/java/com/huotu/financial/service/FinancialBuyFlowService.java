/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.exceptions.NoReachRedeemPeriodException;
import com.huotu.financial.exceptions.NoRedeemStatusException;
import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.BuyFlowModel;
import com.huotu.financial.model.ViewBuyListModel;
import com.huotu.financial.model.ViewFinancialTotalModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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

    /**
     * 可以赎回
     *
     * @param financialBuyFlow
     * @return
     * @throws NoReachRedeemPeriodException
     * @throws NoRedeemStatusException
     * @throws ParseException
     */
    Boolean canRedeem(FinancialBuyFlow financialBuyFlow) throws NoReachRedeemPeriodException, NoRedeemStatusException, ParseException;

    /**
     * 获得购买流水
     * @param userId
     * @param status
     * @param lastTime
     * @return
     */
    List<ViewBuyListModel> getBuyFlowList(Long userId, Integer status, Long lastTime);

    /**
     * 获得累计数据
     * @param userId
     * @return
     */
    ViewFinancialTotalModel total(Long userId);

    BuyFlowModel changeDomainToModel(FinancialBuyFlow flow) throws IOException, UserException;

    List<BuyFlowModel> changeDomainToModelList(List<FinancialBuyFlow> flows) throws IOException, UserException;
}

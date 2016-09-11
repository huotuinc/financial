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
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.exceptions.NoFindRedeemAmountException;
import com.huotu.financial.exceptions.NoReachRedeemPeriodException;
import com.huotu.financial.exceptions.NoRedeemStatusException;
import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.BuyFlowModel;
import com.huotu.financial.model.ViewBuyListModel;
import com.huotu.financial.model.ViewFinancialTotalModel;
import com.huotu.huobanplus.common.entity.OrderItems;
import com.huotu.huobanplus.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
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
    void handleRedeem(FinancialBuyFlow financialBuyFlow) throws NoFindRedeemAmountException, ParseException, NoRedeemStatusException, NoReachRedeemPeriodException;

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
     *
     * @param userId
     * @param status
     * @param lastTime
     * @return
     */
    List<ViewBuyListModel> getBuyFlowList(Long userId, Integer status, Long lastTime);

    /**
     * 获得累计数据
     *
     * @param userId
     * @return
     */
    ViewFinancialTotalModel total(Long userId);

    BuyFlowModel changeDomainToModel(FinancialBuyFlow flow) throws IOException, UserException;

    List<BuyFlowModel> changeDomainToModelList(List<FinancialBuyFlow> flows) throws IOException, UserException;

    /**
     * 获取总收益
     *
     * @param userId
     * @return
     */
    BigDecimal profitTotal(Long userId);

    /**
     * 根据条件查询用户理财列表
     *
     * @param customerId 商户id
     * @param goodId     商品id
     * @param no         理财编号
     * @param pageable   分页
     * @return 用户理财列表
     * @throws IOException
     */
    Page<FinancialBuyFlow> findAllByCustomerIdAndGoodIdAndNo(Long customerId, Long goodId, String no, Pageable pageable) throws IOException;

    /**
     * 创建创建理财编号
     *
     * @param date   当前时间
     * @param userId 用户Id
     * @return
     */
    String createFinancialNo(Date date, Long userId);

    /**
     * 处理付款通知
     * @param userId
     * @param orderId
     */
    void handlePayNotice(Long userId, String orderId);

    /**
     * 保存流水
     * @param financialGoods
     * @param orderItems
     * @param user
     * @param orderId
     */
    void save(FinancialGoods financialGoods, OrderItems orderItems, User user, String orderId);



}

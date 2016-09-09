/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.financial.common.DateHelper;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.exceptions.NoFindRedeemAmountException;
import com.huotu.financial.exceptions.NoReachRedeemPeriodException;
import com.huotu.financial.exceptions.NoRedeemStatusException;
import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.BuyFlowModel;
import com.huotu.financial.model.UserModel;
import com.huotu.financial.model.ViewBuyListModel;
import com.huotu.financial.model.ViewFinancialTotalModel;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.CacheService;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.huobanplus.common.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class FinancialBuyFlowServiceImpl implements FinancialBuyFlowService {


    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FinancialProfitRepository financialProfitRepository;

    @Override
    public void handleRedeem(FinancialBuyFlow financialBuyFlow) throws NoFindRedeemAmountException, ParseException, NoRedeemStatusException, NoReachRedeemPeriodException {

        if (canRedeem(financialBuyFlow)) {
            FinancialBuyFlow findFlow = findRedeem(financialBuyFlow);
            //没有找到可赎回的额度
            if (findFlow == null) throw new NoFindRedeemAmountException("没找到可回购额度");

            findFlow.setIsUsed(true);
            financialBuyFlowRepository.save(findFlow);

            financialBuyFlow.setStatus(FinancialStatus.DOING);
            financialBuyFlowRepository.save(financialBuyFlow);
        }
    }

    @Override
    public BuyFlowModel changeDomainToModel(FinancialBuyFlow flow) throws IOException, UserException {
        BuyFlowModel model = new BuyFlowModel();
        model.setUserId(flow.getUserId());
        UserModel userModel = cacheService.getOneByUserId(flow.getUserId());
        model.setWxName(userModel.getWxName());
        model.setWxImgURL(userModel.getWxImgURL());
        model.setAmount(flow.getAmount());
        model.setBelongOne(flow.getBelongOne());
        model.setBuyTime(flow.getBuyTime());
        model.setCustomerId(flow.getCustomerId());
        model.setFinancialTitle(flow.getFinancialTitle());
        model.setGoodId(flow.getGoodId());
        model.setIsUsed(flow.getIsUsed());
        model.setMoney(flow.getMoney());
        model.setNo(flow.getNo());
        model.setPrice(flow.getPrice());
        model.setRate(flow.getRate());
        model.setRedeemPeriod(flow.getRedeemPeriod());
        model.setStatus(flow.getStatus().ordinal());
        return model;
    }

    @Override
    public List<BuyFlowModel> changeDomainToModelList(List<FinancialBuyFlow> flows) throws IOException, UserException {
        List<BuyFlowModel> list = new ArrayList<>();
        for (FinancialBuyFlow flow : flows) {
            list.add(changeDomainToModel(flow));
        }
        return list;
    }

    /**
     * 是否可赎回
     *
     * @param financialBuyFlow
     * @return
     * @throws
     */
    public Boolean canRedeem(FinancialBuyFlow financialBuyFlow) throws NoReachRedeemPeriodException, NoRedeemStatusException, ParseException {
        if (financialBuyFlow.getStatus() != FinancialStatus.RUNNING) throw new NoRedeemStatusException("不是可回购状态");

        //没到回购日期
        Date date = new Date();
        int day = DateHelper.daysBetween(financialBuyFlow.getBuyTime(), date);
        if (day < financialBuyFlow.getRedeemPeriod()) throw new NoReachRedeemPeriodException("没到回购日期");
        return true;
    }

    private FinancialBuyFlow findRedeem(FinancialBuyFlow financialBuyFlow) {
        //获取此用户是否存在回购的额度
        List<FinancialBuyFlow> financialBuyFlows = financialBuyFlowRepository.findAllForRedeem(financialBuyFlow.getUserId(), financialBuyFlow.getBuyTime(), financialBuyFlow.getMoney());
        if (financialBuyFlows.size() > 0) return financialBuyFlows.get(0);
        return null;
    }

    public List<ViewBuyListModel> getBuyFlowList(Long userId, Integer status, Long lastTime) {
        List<ViewBuyListModel> viewBuyListModels = new ArrayList<>();

        StringBuilder hql = new StringBuilder();
        hql.append("select flow,goods from FinancialBuyFlow flow left join Goods goods on goods.id=flow.goodId where flow.userId=:userId");
        FinancialStatus status1 = FinancialStatus.RUNNING;
        if (status != null) {
            if (status == 1) status1 = FinancialStatus.DOING;
            if (status == 2) status1 = FinancialStatus.REDEEMED;
        }
        hql.append(" and flow.status=:status");
        if (lastTime != null && lastTime > 0) {
            hql.append(" and flow.buyTime<:buyTime");
        }
        hql.append(" order by flow.buyTime desc");
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("userId", userId);
        query.setParameter("status", status1);
        if (lastTime != null && lastTime > 0) query.setParameter("buyTime", new Date(lastTime));
        query.setMaxResults(10);
        List list = query.getResultList();
        for (Object object : list) {
            Object[] objects = (Object[]) object;
            FinancialBuyFlow financialBuyFlow = (FinancialBuyFlow) objects[0];
            Goods goods = (Goods) objects[1];
            ViewBuyListModel viewBuyListModel = new ViewBuyListModel();
            viewBuyListModel.setTitle(financialBuyFlow.getFinancialTitle());
            viewBuyListModel.setPrice(financialBuyFlow.getPrice());
            viewBuyListModel.setAmount(financialBuyFlow.getAmount());
            viewBuyListModel.setNo(financialBuyFlow.getNo());
            viewBuyListModel.setImageUrl(goods.getImages().get(0).getSmallPic().getValue());
            viewBuyListModel.setMoeny(financialBuyFlow.getMoney());
            Boolean canReddm = false;
            try {
                canReddm = canRedeem(financialBuyFlow);
            } catch (Exception ex) {
            }
            if (canReddm)
                viewBuyListModel.setStatus(1);
            else
                viewBuyListModel.setStatus(0);
            viewBuyListModel.setDate(financialBuyFlow.getBuyTime());
            viewBuyListModels.add(viewBuyListModel);
        }

        return viewBuyListModels;
    }


    public ViewFinancialTotalModel total(Long userId) {
        ViewFinancialTotalModel viewFinancialTotalModel = new ViewFinancialTotalModel();

        BigDecimal money = financialBuyFlowRepository.countFinancialMoney(userId, FinancialStatus.REDEEMED);
        viewFinancialTotalModel.setMoney(money == null ? new BigDecimal(0) : money);

        BigDecimal yesterdayIncome = financialProfitRepository.countYestodayProfit(userId
                , com.huotu.common.base.DateHelper.getYesterdayBegin()
                , com.huotu.common.base.DateHelper.getThisDayBegin());
        viewFinancialTotalModel.setYesterdayIncome(yesterdayIncome == null ? new BigDecimal(0) : yesterdayIncome);

        BigDecimal totalIncome = financialProfitRepository.countTotalProfit(userId);
        viewFinancialTotalModel.setTotalIncome(totalIncome == null ? new BigDecimal(0) : totalIncome);
        return viewFinancialTotalModel;
    }

    @Override
    public BigDecimal profitTotal(Long userId) {
        return financialProfitRepository.countTotalProfit(userId);
    }


}

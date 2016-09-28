/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.common.base.StringHelper;
import com.huotu.financial.common.DateHelper;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.entity.FinancialReturnRefund;
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
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.CacheService;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.common.entity.OrderItems;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.OrderItemsRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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


    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonConfigsService commonConfigsService;


    @Override
    public void handleRedeem(FinancialBuyFlow financialBuyFlow) throws NoFindRedeemAmountException, ParseException, NoRedeemStatusException, NoReachRedeemPeriodException {

        if (canRedeem(financialBuyFlow)) {
            FinancialBuyFlow findFlow = findRedeem(financialBuyFlow);
            //没有找到可赎回的额度
            if (findFlow == null) throw new NoFindRedeemAmountException("没找到可抵用额度");

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
        model.setLoginName(userModel.getLoginName());
        if (null != flow.getRefund()) {
            FinancialReturnRefund refund = flow.getRefund();
            model.setPhone(refund.getPhone());
            model.setLogisticalName(refund.getLogisticalName());
            model.setLogisticalCode(refund.getLogisticalCode());
        }
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

    @Override
    public Boolean canRedeemNew(FinancialBuyFlow financialBuyFlow) throws NoReachRedeemPeriodException,
            NoRedeemStatusException, ParseException, NoFindRedeemAmountException {
        if (financialBuyFlow.getStatus() != FinancialStatus.RUNNING) throw new NoRedeemStatusException("不是可回购状态");

        FinancialBuyFlow findFlow = findRedeem(financialBuyFlow);
        //没有找到可赎回的额度
        if (findFlow == null) throw new NoFindRedeemAmountException("没找到可抵用额度");
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
            viewBuyListModel.setImageUrl(commonConfigsService.getPicURL() + goods.getImages().get(0).getSmallPic().getValue());
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
            if (financialBuyFlow.getStatus().equals(FinancialStatus.RUNNING))
                viewBuyListModel.setOwnerStatus(0);
            else if (financialBuyFlow.getStatus().equals(FinancialStatus.DOING))
                viewBuyListModel.setOwnerStatus(1);
            else if (financialBuyFlow.getStatus().equals(FinancialStatus.REDEEMED))
                viewBuyListModel.setOwnerStatus(2);
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

    @Override
    public Page<FinancialBuyFlow> findAllByCustomerIdAndGoodIdAndNoAndUserId(Long customerId, Long goodId, String no,
                                                                             Long userId, Pageable pageable) throws IOException {
        return financialBuyFlowRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.and(cb.equal(root.get("customerId").as(Long.class), customerId),
                    cb.equal(root.get("goodId").as(Long.class), goodId));
            if (!StringUtils.isEmpty(no))
                predicate = cb.and(predicate, cb.like(root.get("no").as(String.class), "%" + no + "%"));
            if (null != userId)
                predicate = cb.and(predicate, cb.like(root.get("userId").as(String.class), "%" + no + "%"));
            return predicate;
        }, pageable);
    }

    @Override
    public Page<FinancialBuyFlow> findAllByCustomerIdAndNoAndStatus(Long customerId, String no, FinancialStatus status,
                                                                    Pageable pageable) throws IOException {
        return financialBuyFlowRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.and(cb.equal(root.get("customerId").as(Long.class), customerId));
            if (!StringUtils.isEmpty(no))
                predicate = cb.and(predicate, cb.like(root.get("no").as(String.class), "%" + no + "%"));
            if (!StringUtils.isEmpty(status))
                predicate = cb.and(predicate, cb.equal(root.get("status").as(FinancialStatus.class), status));
            return predicate;
        }, pageable);
    }


    public String createFinancialNo(Date date, Long userId) {
        //订单号
        Random random = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd" + StringHelper.randomNum(random, 8));
        return simpleDateFormat.format(date) + userId.toString();
    }


    public void handlePayNotice(Long userId, String orderId) {
        User user = userRepository.findOne(userId);
        List<OrderItems> orderItemses = orderItemsRepository.findAllByOrder_Id(orderId);
        for (OrderItems orderItems : orderItemses) {
            FinancialGoods financialGoods = financialGoodsRepository.findOne(Long.parseLong(orderItems.getGoodsId().toString()));
            if (financialGoods != null) {
                save(financialGoods, orderItems, user, orderId);
            }
        }
    }

    public void save(FinancialGoods financialGoods, OrderItems orderItems, User user, String orderId) {
        Date date = new Date();
        String no = createFinancialNo(date, user.getId());
        FinancialBuyFlow financialBuyFlow = new FinancialBuyFlow();
        financialBuyFlow.setUserId(user.getId());
        financialBuyFlow.setGoodId(financialGoods.getId());
        financialBuyFlow.setPrice(new BigDecimal(orderItems.getPrice()));
        financialBuyFlow.setAmount(orderItems.getAmount());
        financialBuyFlow.setMoney(new BigDecimal(orderItems.getAmount()).multiply(new BigDecimal(orderItems.getPrice())));
        financialBuyFlow.setBelongOne(user.getBelongOne());
        financialBuyFlow.setBuyTime(date);
        financialBuyFlow.setCustomerId(user.getMerchant().getId());
        financialBuyFlow.setFinancialTitle(financialGoods.getTitle());
        financialBuyFlow.setIsUsed(false);
        financialBuyFlow.setNo(no);
        financialBuyFlow.setRate(financialGoods.getRate());
        financialBuyFlow.setRedeemPeriod(financialGoods.getRedeemPeriod());
        financialBuyFlow.setStatus(FinancialStatus.RUNNING);
        financialBuyFlow.setToOuterOrderNo(orderId);
        financialBuyFlowRepository.save(financialBuyFlow);
    }
}

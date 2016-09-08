package com.huotu.financial.service.impl;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialProfit;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.model.ViewProfitListModel;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.FinancialProfitService;
import com.huotu.huobanplus.common.entity.MallAdvanceLogs;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.MallAdvanceLogsRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class FinancialProfitServiceImpl implements FinancialProfitService {

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Autowired
    private FinancialProfitRepository financialProfitRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MallAdvanceLogsRepository mallAdvanceLogsRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void countProfit() {
        List<FinancialBuyFlow> financialBuyFlows = financialBuyFlowRepository.findAllByStatus(FinancialStatus.RUNNING);
        for (FinancialBuyFlow financialBuyFlow : financialBuyFlows) {
            doOneBuy(financialBuyFlow);
        }
    }

    @Transactional
    private void doOneBuy(FinancialBuyFlow financialBuyFlow) {
        BigDecimal profit = financialBuyFlow.getMoney().multiply(financialBuyFlow.getRate());
        Date date = new Date();
        User user = userRepository.findOne(financialBuyFlow.getUserId());

        //理财收益
        FinancialProfit financialProfit = new FinancialProfit();
        financialProfit.setCustomerId(financialBuyFlow.getCustomerId());
        financialProfit.setMoney(profit);
        financialProfit.setNo(financialBuyFlow.getNo());
        financialProfit.setTime(date);
        financialProfit.setUserId(financialBuyFlow.getUserId());
        financialProfitRepository.save(financialProfit);

        //用户流水
        MallAdvanceLogs mallAdvanceLogs = new MallAdvanceLogs();
        mallAdvanceLogs.setCustomerId(financialBuyFlow.getCustomerId());
        mallAdvanceLogs.setDisabled(0);
        mallAdvanceLogs.setMoney(profit.doubleValue());
        mallAdvanceLogs.setImportMoney(profit.doubleValue());
        mallAdvanceLogs.setMemberAdvance(user.getUserBalance());
        mallAdvanceLogs.setMemberId(financialBuyFlow.getUserId());
        mallAdvanceLogs.setMTime(date);
        mallAdvanceLogs.setOrderId(financialBuyFlow.getNo());
        mallAdvanceLogs.setPayMethod("理财收益");
        mallAdvanceLogs.setMemo("理财收益");
        mallAdvanceLogsRepository.save(mallAdvanceLogs);


        //用户余额
        user.setUserBalance((new BigDecimal(user.getUserBalance()).add(profit)).doubleValue());
        userRepository.saveAndFlush(user);

    }

    public List<ViewProfitListModel> profitList(Long userId, Long lastId) {
        List<ViewProfitListModel> viewProfitListModels = new ArrayList<>();

        StringBuilder hql = new StringBuilder();
        hql.append("select profit from FinancialProfit profit where profit.userId=:userId");
        if (lastId != null && lastId > 0) hql.append(" and profit.id<:id");
        hql.append(" order by profit.id desc");
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("userId", userId);
        if (lastId != null && lastId > 0) query.setParameter("id", lastId);
        query.setMaxResults(10);
        List list = query.getResultList();
        for (Object object : list) {
            FinancialProfit financialProfit = (FinancialProfit) object;
            ViewProfitListModel viewProfitListModel = new ViewProfitListModel();
            viewProfitListModel.setDate(financialProfit.getTime());
            viewProfitListModel.setMoney(financialProfit.getMoney());
            viewProfitListModel.setNo(financialProfit.getNo());
            viewProfitListModels.add(viewProfitListModel);
        }
        return viewProfitListModels;
    }
}

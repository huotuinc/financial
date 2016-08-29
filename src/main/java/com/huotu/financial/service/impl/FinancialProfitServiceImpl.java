package com.huotu.financial.service.impl;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialProfit;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.FinancialProfitService;
import com.huotu.huobanplus.common.entity.MallAdvanceLogs;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.MallAdvanceLogsRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class FinancialProfitServiceImpl implements FinancialProfitService {

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Autowired
    private FinancialProfitRepository financialProfitRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MallAdvanceLogsRepository mallAdvanceLogsRepository;

    @Override
    public void countProfit() {
        List<FinancialBuyFlow> financialBuyFlows = financialBuyFlowRepository.findAllByStatus(FinancialStatus.GOING);
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
}

package com.huotu.financial.service.impl;

import com.huotu.financial.common.DateHelper;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.exceptions.NoFindRedeemAmountException;
import com.huotu.financial.exceptions.NoReachRedeemPeriodException;
import com.huotu.financial.exceptions.NoRedeemStatusException;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.service.FinancialBuyFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class FinancialBuyFlowServiceImpl implements FinancialBuyFlowService {


    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Override
    public void handleRedeem(FinancialBuyFlow financialBuyFlow) throws NoFindRedeemAmountException, ParseException, NoRedeemStatusException, NoReachRedeemPeriodException {

        if (canRedeem(financialBuyFlow)) {
            FinancialBuyFlow findFlow = findRedeem(financialBuyFlow);
            //没有找到可赎回的额度
            if (findFlow == null) throw new NoFindRedeemAmountException("没找到可回购额度");

            findFlow.setIsUsed(true);
            financialBuyFlowRepository.save(findFlow);

            financialBuyFlow.setStatus(FinancialStatus.APPLY);
            financialBuyFlowRepository.save(financialBuyFlow);
        }
    }

    /**
     * 是否可赎回
     *
     * @param financialBuyFlow
     * @return
     * @throws
     */
    public Boolean canRedeem(FinancialBuyFlow financialBuyFlow) throws NoReachRedeemPeriodException, NoRedeemStatusException, ParseException {
        if (financialBuyFlow.getStatus() != FinancialStatus.GOING) throw new NoRedeemStatusException("不是可回购状态");

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
}

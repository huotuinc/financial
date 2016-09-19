/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.service.FinancialGoodsService;
import com.huotu.huobanplus.common.entity.MallAdvanceLogs;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.MallAdvanceLogsRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by administrator on 2016/8/30.
 */
@Service
public class FinancialGoodsServiceImpl implements FinancialGoodsService {

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MallAdvanceLogsRepository mallAdvanceLogsRepository;

    @Transactional
    @Override
    public synchronized ModelMap updateFlowStatus(String no) throws IOException {
        FinancialBuyFlow flow = financialBuyFlowRepository.getOne(no);
        ModelMap map = new ModelMap();
//        if (!flow.getStatus().equals(FinancialStatus.DOING)) {
//            map.addAttribute("success", false);
//            map.addAttribute("msg", "赎回失败，请核对信息后再进行操作");
//            return map;
//        }

        flow.setStatus(FinancialStatus.REDEEMED);
        financialBuyFlowRepository.save(flow);

        User user = userRepository.getOne(flow.getUserId());
        Double userBalance = user.getUserBalance();

        user.setUserBalance(new BigDecimal(userBalance).add(flow.getMoney()).doubleValue());
        userRepository.save(user);
        save(user, flow.getMoney(), flow);
        map.addAttribute("success", true);
        return map;
    }

    /**
     * 交易流水保存
     *
     * @param user  用户
     * @param money 交易金额
     * @param flow  用户理财产品
     * @return 交易流水
     * @throws IOException
     */
    private MallAdvanceLogs save(User user, BigDecimal money, FinancialBuyFlow flow) throws IOException {
        MallAdvanceLogs mallAdvanceLogs = new MallAdvanceLogs();
        mallAdvanceLogs.setMemberId(user.getId());
        mallAdvanceLogs.setMoney(money);
        mallAdvanceLogs.setMessage("");
        mallAdvanceLogs.setMTime(new Date());
        mallAdvanceLogs.setPaymentId("");
        mallAdvanceLogs.setOrderId(flow.getNo());
        mallAdvanceLogs.setPayMethod("理财产品赎回");
        String memo = "您赎回了理财产品" + flow.getNo();
        mallAdvanceLogs.setMemo(memo);
        mallAdvanceLogs.setImportMoney(money.doubleValue());
        mallAdvanceLogs.setShopAdvance(0.0);
        mallAdvanceLogs.setExplodeMoney(0.0);
        mallAdvanceLogs.setMemberAdvance(0.0);
        mallAdvanceLogs.setDisabled(0);
        mallAdvanceLogs.setCustomerId(user.getMerchant().getId());
        mallAdvanceLogsRepository.save(mallAdvanceLogs);
        return mallAdvanceLogs;
    }
}

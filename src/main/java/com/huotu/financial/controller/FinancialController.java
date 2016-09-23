/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.controller;

import com.huotu.financial.common.PublicParameterHolder;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.model.ViewBuyListModel;
import com.huotu.financial.model.ViewFinancialTotalModel;
import com.huotu.financial.model.ViewProfitListModel;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.FinancialProfitService;
import com.huotu.huobanplus.common.entity.MerchantConfig;
import com.huotu.huobanplus.common.repository.MerchantConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
@Controller
@RequestMapping("/financial")
public class FinancialController {

    @Autowired
    private FinancialBuyFlowService financialBuyFlowService;

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Autowired
    private FinancialProfitService financialProfitService;

    @Autowired
    private MerchantConfigRepository merchantConfigRepository;


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/financial/index";
    }

    @RequestMapping(value = "/profit", method = RequestMethod.GET)
    public String profit() {
        return "/financial/profit";
    }

    /**
     * 理财中心列表
     *
     * @param status   默认0，0 持有中 1处理中 2赎回
     * @param lastTime 最后一条时间
     * @return
     */
    @RequestMapping(value = "/financialList")
    @ResponseBody
    public List<ViewBuyListModel> financialList(@RequestParam(value = "status") Integer status
            , @RequestParam(value = "lastTime", required = false) Long lastTime) {
        Long userId = PublicParameterHolder.get().getUserId();
        return financialBuyFlowService.getBuyFlowList(userId, status, lastTime);
    }

    /**
     * 理财中心汇总
     *
     * @return
     */
    @RequestMapping(value = "/financialTotal", method = RequestMethod.GET)
    @ResponseBody
    public ViewFinancialTotalModel financialTotal() {
        Long userId = PublicParameterHolder.get().getUserId();
        return financialBuyFlowService.total(userId);
    }

    @RequestMapping(value = "/profitTotal", method = RequestMethod.GET)
    @ResponseBody
    public BigDecimal profitTotal() {
        Long userId = PublicParameterHolder.get().getUserId();
        return financialBuyFlowService.profitTotal(userId);
    }

    @RequestMapping(value = "/profitList")
    @ResponseBody
    public List<ViewProfitListModel> profitList(Long lastId) {
        Long userId = PublicParameterHolder.get().getUserId();
        return financialProfitService.profitList(userId, lastId);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "/financial/test";
    }

    @RequestMapping(value = "/returnRefund")
    public String returnRefund(@RequestParam String no, Model model) {
        FinancialBuyFlow flow = financialBuyFlowRepository.findOne(no);
        MerchantConfig config = merchantConfigRepository.findByMerchantId(flow.getCustomerId());
//        model
        model.addAttribute("flow", flow);
        model.addAttribute("defaultReturnAddress", config.getDefaultReturnAddress());
        model.addAttribute("isReturning", flow.getStatus().equals(FinancialStatus.RUNNING) ? false : true);
        return "/financial/returnRefund";
    }
}

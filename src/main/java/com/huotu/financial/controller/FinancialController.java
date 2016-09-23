package com.huotu.financial.controller;

import com.huotu.financial.common.PublicParameterHolder;
import com.huotu.financial.model.ViewBuyListModel;
import com.huotu.financial.model.ViewFinancialTotalModel;
import com.huotu.financial.model.ViewProfitListModel;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.FinancialProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private FinancialProfitService financialProfitService;


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
}

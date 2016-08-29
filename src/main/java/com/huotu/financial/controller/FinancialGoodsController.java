/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.controller;

import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.util.RestUtil;
import com.huotu.financial.util.support.BasicNameValuePair;
import com.huotu.huobanplus.sdk.mall.annotation.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/29.
 */
@Controller
@RequestMapping(value = "/financialGoods")
public class FinancialGoodsController {

    private static final int pageSize = 20;

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;
    @Autowired
    private CommonConfigsService commonConfigsService;

    /**
     * 理财列表页
     *
     * @param customerId 商户id
     * @return /financial/index.html
     * @throws IOException
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@CustomerId Long customerId, @RequestParam(required = false) Integer pageNo) throws IOException {
        if (Objects.isNull(pageNo))
            pageNo = 0;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);
        Page<FinancialGoods> pages = financialGoodsRepository.findAllByCustomerId(customerId, pageable);
        return RestUtil.success("/financial/index",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("pages", pages));
    }

    /**
     * 新增活动页面
     *
     * @param customerId 商户id
     * @return /financial/financialPage.html
     * @throws IOException
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public ModelAndView addPage(@CustomerId Long customerId) throws IOException {
        return RestUtil.success("/financial/financialPage",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("isEdit", false));
    }

    /**
     * 编辑活动页面
     *
     * @param customerId 商户id
     * @return /financial/financialPage.html
     * @throws IOException
     */
    @RequestMapping(value = "/editPage", method = RequestMethod.GET)
    public ModelAndView editPage(@CustomerId Long customerId) throws IOException {
        return RestUtil.success("/financial/financialPage",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("isEdit", true));
    }

    /**
     * 保存理财活动
     *
     * @param id           活动id
     * @param customerId   商户id
     * @param title        活动名称
     * @param rate         日利率
     * @param redeemPeriod 赎回周期
     * @return 保存结果
     * @throws IOException
     */
    @Transactional
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView save(
            @RequestParam(required = false) Long id, @RequestParam Long customerId, @RequestParam String title,
            @RequestParam BigDecimal rate, @RequestParam int redeemPeriod
    ) throws IOException {
        FinancialGoods financialGoods;
        if (Objects.nonNull(id))
            financialGoods = financialGoodsRepository.getOne(id);
        else
            financialGoods = new FinancialGoods();
        financialGoods.setCustomerId(customerId);
        financialGoods.setTitle(title);
        financialGoods.setRate(rate);
        financialGoods.setRedeemPeriod(redeemPeriod);
        financialGoodsRepository.save(financialGoods);
        return RestUtil.success(null, new BasicNameValuePair("success", true),
                new BasicNameValuePair("financialGoods", financialGoods),
                new BasicNameValuePair("url", getIndexURL()));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam Long id) throws IOException {
        financialGoodsRepository.delete(id);
        return RestUtil.success(null, new BasicNameValuePair("success", true),
                new BasicNameValuePair("url", getIndexURL()));
    }

    /**
     * 得到列表URL
     *
     * @return
     */
    private String getIndexURL() {
        String url = this.getClass().getAnnotation(RequestMapping.class).value()[0];
        String webURL = commonConfigsService.getWebUrl();
        return webURL + url;
    }

    /**
     * 检查该商品是否被之前的活动使用过
     *
     * @param id 商户id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/checkGoodsUsed", method = RequestMethod.GET)
    public boolean checkGoodsUsed(@RequestParam Long id) throws IOException {
        Long count = financialGoodsRepository.countById(id);
        return count.intValue() <= 0;
    }
}

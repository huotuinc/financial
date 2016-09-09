/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.controller;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.entity.FinancialProfit;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.FinancialGoodsService;
import com.huotu.financial.util.RestUtil;
import com.huotu.financial.util.support.BasicNameValuePair;
import com.huotu.huobanplus.common.dataService.GoodsService;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.common.repository.GoodsRepository;
import com.huotu.huobanplus.sdk.mall.annotation.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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

//    private static final int pageSize = 20;

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;
    @Autowired
    private CommonConfigsService commonConfigsService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;
    @Autowired
    private FinancialProfitRepository financialProfitRepository;
    //    @Autowired
//    private UserService userService;
    @Autowired
    private FinancialGoodsService financialGoodsService;

    /**
     * 理财列表页
     *
     * @param customerId 商户id
     * @return /manage/index.html
     * @throws IOException
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@CustomerId Long customerId, @RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer pageSize) throws IOException {
        if (Objects.isNull(page))
            page = 1;
        if (Objects.isNull(pageSize))
            pageSize = 20;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<FinancialGoods> pages = financialGoodsRepository.findAllByCustomerId(customerId, pageable);
        return RestUtil.success("/manage/index",
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
        return RestUtil.success("/manage/financialPage",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("isEdit", false));
    }

    /**
     * 编辑活动页面
     *
     * @param customerId 商户id
     * @return /manage/financialPage.html
     * @throws IOException
     */
    @RequestMapping(value = "/editPage", method = RequestMethod.GET)
    public ModelAndView editPage(@CustomerId Long customerId) throws IOException {
        return RestUtil.success("/manage/financialPage",
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

    /**
     * 删除理财活动
     *
     * @param id 活动id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
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
    @ResponseBody
    public boolean checkGoodsUsed(@RequestParam Long id) throws IOException {
        Long count = financialGoodsRepository.countById(id);
        return count.intValue() == 0;
    }

    /**
     * 查询商品列表
     *
     * @param customerId 商户id
     * @return 商品列表
     * @throws IOException
     */
//    @RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
//    @ResponseBody
//    public Page<Goods> getGoodsList(@CustomerId Long customerId) throws IOException {
//        Pageable pageable = new PageRequest(0, 20);
//        Page<Goods> pages = goodsRepository.findByOwner_IdAndScenesAndDisabledFalseAndMarketableTrue(customerId, 0, pageable);
//        return pages;
//    }
    @SuppressWarnings("SpellCheckingInspection")
    @RequestMapping(value = "/getGoodsList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ModelMap getGoodsList(@CustomerId Long customerId, @RequestParam(value = "page") int page,
                                 @RequestParam(value = "pagesize") int pageSize) throws IOException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<Goods> pages = goodsRepository.findByOwner_IdAndScenesAndDisabledFalseAndMarketableTrue(customerId, 0, pageable);
//        Page<Goods> pages = goodsService.
        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        ModelMap map = new ModelMap();
        map.addAttribute("Total", pages.getTotalElements());
        map.addAttribute("PageSize", pageSize);
        map.addAttribute("Rows", pages.getContent());
        map.addAttribute("PageIndex", page);
        map.addAttribute("PageCount", pageCount);
        return map;
    }


    /**
     * 用户理财产品详情页
     *
     * @param customerId 商户id
     * @param id         商品id
     * @return /manage/buyFlowIndex.html
     * @throws IOException
     */
    public ModelAndView buyFlowIndex(@CustomerId Long customerId, @RequestParam Long id) throws IOException {
        return RestUtil.success("/manage/buyFlowIndex", new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("goodsId", id));
    }

    /**
     * 用户理财列表
     *
     * @param page       页码
     * @param pageSize   每页条数
     * @param customerId 商户id
     * @return 用户理财列表
     * @throws IOException
     */
    @RequestMapping(value = "/getPageBuyFlow", method = RequestMethod.GET)
    @ResponseBody
    public Page<FinancialBuyFlow> getPageBuyFlow(@RequestParam int page, @RequestParam int pageSize,
                                                 @CustomerId Long customerId) throws IOException {
        Sort sort = new Sort(Sort.Direction.DESC, "buyTime");
        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page<FinancialBuyFlow> pages = financialBuyFlowRepository.findAllByCustomerId(customerId, pageable);
        return pages;
    }

    /**
     * 根据用户和理财期号查询用户每日流水列表
     *
     * @param page     页码
     * @param pageSize 每日条数
     * @param userId   用户id
     * @param no       期号
     * @return 流水列表
     * @throws IOException
     */
    @RequestMapping(value = "/getPageProfit", method = RequestMethod.GET)
    @ResponseBody
    public Page<FinancialProfit> getPageProfit(@RequestParam int page, @RequestParam int pageSize,
                                               @RequestParam Long userId, @RequestParam String no) throws IOException {
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page<FinancialProfit> pages = financialProfitRepository.findAllByUserIdAndNo(userId, no, pageable);

        return pages;
    }

    /**
     * 同意理财赎回操作
     *
     * @param no
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/updateFlowStatus", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView updateFlowStatus(@RequestParam String no) throws IOException {
        return financialGoodsService.updateFlowStatus(no);

    }
}

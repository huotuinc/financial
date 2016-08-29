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
import com.huotu.huobanplus.sdk.mall.annotation.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/8/29.
 */
@Controller
@RequestMapping(value = "/financialGoods")
public class FinancialGoodsController {

    private static final int pageSize = 20;

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;

    /**
     * 理财列表页
     *
     * @param customerId 商户id
     * @return 理财列表页
     * @throws IOException
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@CustomerId Long customerId, @RequestParam(required = false) Integer pageNo) throws IOException {
        if (Objects.isNull(pageNo))
            pageNo = 0;
        ModelAndView modelAndView = new ModelAndView();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);
        Page<FinancialGoods> pages = financialGoodsRepository.findAllByCustomerId(customerId, pageable);
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("customerId", customerId);
        map.put("pages", pages);
        modelAndView.addAllObjects(map);
        modelAndView.setViewName("/financial/index");
        return modelAndView;
    }


}

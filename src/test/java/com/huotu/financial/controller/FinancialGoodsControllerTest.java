/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.controller;

import com.huotu.financial.WebTest;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.huobanplus.common.entity.Goods;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by administrator on 2016/8/29.
 */
@Transactional(value = "transactionManager")
public class FinancialGoodsControllerTest extends WebTest {

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;

    private Random random = new Random();

    @Test
    @Rollback
    public void index() throws Exception {
        ModelAndView view = mockMvc.perform(get("/financialGoods/index"))
                .andExpect(status().isOk()).andReturn().getModelAndView();
        assertEquals("", view.getViewName(), "/financial/index");
        Map<String, Object> map = view.getModel();
        assertEquals("", ((Page<FinancialGoods>) map.get("pages")).getTotalElements(), 0);
        Long customerId = Long.parseLong(map.get("customerId").toString());
        Random random = new Random();
        int size = random.nextInt(10);
        for (int i = 0; i < size; i++) {
            FinancialGoods goods = new FinancialGoods();
            goods.setId((long) i + 10);
            goods.setTitle(UUID.randomUUID().toString());
            goods.setCustomerId(customerId);
            financialGoodsRepository.save(goods);
        }
        ModelAndView newView = mockMvc.perform(get("/financialGoods/index"))
                .andExpect(status().isOk()).andReturn().getModelAndView();
        Map<String, Object> newMap = newView.getModel();
        assertEquals("", ((Page<FinancialGoods>) newMap.get("pages")).getTotalElements(), size);
    }

    @Test
    public void addPage() throws Exception {

    }

    @Test
    @Rollback
    public void checkGoodsUsed() throws Exception {
        Goods goods = randomGoods();
        String status = mockMvc.perform(get("/financialGoods/checkGoodsUsed")
                .param("id", goods.getId().toString()))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("", status, "true");
        randomFinancialGoods(goods.getId());
        String newStatus = mockMvc.perform(get("/financialGoods/checkGoodsUsed")
                .param("id", goods.getId().toString()))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("", newStatus, "false");
    }


    @Test
    @Rollback
    public void buyFlowIndex() throws Exception {
        Goods goods = randomGoods();
        FinancialGoods financialGoods = randomFinancialGoods(goods.getId());
        ModelAndView view = mockMvc.perform(get("/financialGoods/buyFlowIndex")
                .param("id", financialGoods.getId().toString()))
                .andExpect(status().isOk()).andReturn().getModelAndView();
        Map<String, Object> map = view.getModel();
        assertEquals("", Long.parseLong(String.valueOf(map.get("total"))), 0L);
        int size = random.nextInt(10) + 20;
        int i = 0;
        List<FinancialBuyFlow> list = new LinkedList<>();
        while (i < size) {
            FinancialBuyFlow flow = randomFinancialBuyFlow(financialGoods.getId());
            list.add(flow);
            i++;
        }
        ModelAndView newView = mockMvc.perform(get("/financialGoods/buyFlowIndex")
                .param("id", financialGoods.getId().toString()))
                .andExpect(status().isOk()).andReturn().getModelAndView();
        Map<String, Object> newMap = newView.getModel();
        assertEquals("", Long.parseLong(String.valueOf(newMap.get("total"))), list.size());
    }
}

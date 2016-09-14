/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial;

import com.huotu.financial.boot.MvcConfig;
import com.huotu.financial.boot.SystemConfig;
import com.huotu.financial.common.PublicParameterHolder;
import com.huotu.financial.common.PublicParameterModel;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.GoodsRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import libspringtest.SpringWebTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

/**
 * Created by administrator on 2016/8/29.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@ActiveProfiles(value = {"development", "develop", "test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, SystemConfig.class})
@WebAppConfiguration
public abstract class WebTest extends SpringWebTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private Environment env;

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取当前登录的user
     *
     * @return
     */
    public Long getCurrentUserId() {
        PublicParameterModel ppm = PublicParameterHolder.get();
        Long userId = ppm.getUserId();
        return userId;
    }

    public Long getCurrentCustomerId() {
        Long customerId;
        if (env.acceptsProfiles("development") || env.acceptsProfiles("develop")) {
            customerId = 4471L;
        } else if (env.acceptsProfiles("test"))
            customerId = 3347L;
        else return null;
        return customerId;
    }

    public Goods randomGoods() throws Exception {
        Goods goods = new Goods();
        goods.setTitle(UUID.randomUUID().toString());
        goodsRepository.saveAndFlush(goods);
        return goods;
    }

    public User randomUser() throws Exception {
        User user = new User();
        user.setWxNickName(UUID.randomUUID().toString());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        userRepository.saveAndFlush(user);
        return user;
    }

    public FinancialGoods randomFinancialGoods(Long id) throws Exception {
        FinancialGoods financialGoods = new FinancialGoods();
        financialGoods.setId(id);
        financialGoods.setTitle(UUID.randomUUID().toString());
        financialGoodsRepository.saveAndFlush(financialGoods);
        return financialGoods;
    }

    public FinancialBuyFlow randomFinancialBuyFlow(Long goodsId) throws Exception {
        FinancialBuyFlow flow = new FinancialBuyFlow();
        flow.setGoodId(goodsId);
        flow.setNo(UUID.randomUUID().toString());
        flow.setCustomerId(getCurrentCustomerId());
        User user = randomUser();
        flow.setUserId(user.getId());
        flow.setStatus(FinancialStatus.REDEEMED);
        financialBuyFlowRepository.saveAndFlush(flow);
        return flow;
    }
}

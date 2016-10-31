/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.financial.service.CommonConfigsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class CommonConfigsServiceImpl implements CommonConfigsService {

    @Autowired
    Environment env;

    @Override
    public String getWebUrl() {
        return env.getProperty("financial.weburl", "http://finance.fanfancat.com:8088");
    }

    @Override
    public String getAuthWebUrl() {
        return env.getProperty("auth.web.url", "http://sso.51flashmall.com:8091");
    }

    @Override
    public String getAuthKeySecret() {
        return env.getProperty("auth.appsecrect", "1165a8d240b29af3f418b8d10599d0dc");
    }

    public String getAppSecret() {
        return env.getProperty("appsecrect", "9389e8a5c32eefa3134340640fb4ceaa");
    }

    @Override
    public String getMallResourceServerUrl() {
        return env.getProperty("mall.resource.serverUrl", "http://res.51flashmall.com");
    }



    @Override
    public String getAppUseSecret() {
        return env.getProperty("appusesecret", "08afd6f9ae0c6017d105b4ce580de885");
    }

    @Override
    public String getMainDomain() {
        return env.getProperty("maindomain", "51flashmall.com");
    }

}

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
        return env.getProperty("sisweb.weburl", "http://192.168.1.57:8080");
    }

    @Override
    public String getAuthWebUrl() {
        return env.getProperty("auth.web.url", "http://test.auth.huobanplus.com:8081");
    }

    @Override
    public String getAuthKeySecret() {
        return env.getProperty("auth.appsecrect", "1165a8d240b29af3f418b8d10599d0dc");
    }

    public String getAppSecret() {
        return env.getProperty("appsecrect", "9389e8a5c32eefa3134340640fb4ceaa");
    }
}

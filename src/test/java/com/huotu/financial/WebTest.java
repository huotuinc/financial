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
import libspringtest.SpringWebTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by administrator on 2016/8/29.
 */
@ActiveProfiles(value = {"development", "develop", "test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, SystemConfig.class})
@WebAppConfiguration
public abstract class WebTest extends SpringWebTest {

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
}

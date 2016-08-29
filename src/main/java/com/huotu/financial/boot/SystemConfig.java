/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.boot;

import com.huotu.huobanplus.base.ResourceConfig;
import com.huotu.huobanplus.common.DataServiceSpringConfig;
import com.huotu.huobanplus.common.entity.MallBaseConfig;
import me.jiangcai.lib.spring.data.ClassicsRepositoryFactoryBean;
import me.jiangcai.lib.spring.logging.LoggingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Administrator on 2016/8/28.
 */
@Configuration
@ImportResource("classpath:spring-jpa.xml")
@EnableJpaRepositories(value = "com.huotu.financial.repository", repositoryFactoryBeanClass = ClassicsRepositoryFactoryBean.class)
@Import(value = {
        LoggingConfig.class,
        DataServiceSpringConfig.class,
        MallBaseConfig.class,
        ResourceConfig.class})
public class SystemConfig {
}

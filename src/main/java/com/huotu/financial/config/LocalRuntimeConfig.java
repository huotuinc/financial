/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Created by jin on 2016/8/23.
 */
@Profile("!container")
@Configuration
@ImportResource("classpath:datasource.xml")
public class LocalRuntimeConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("120.24.243.104");
        factory.setPort(6380);
        factory.setDatabase(10);
        factory.setPassword("k2928jdjh37ejGHUjq82jdjfuKOOOO93jdckr8xnvbuYy38djU38zjOYTNC1838djaj");
        return factory;
    }
}

/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Created by jin on 2016/8/23.
 */
@Profile("container")
@Configuration
@ImportResource("classpath:containerdatasource.xml")
public class ContainerRuntimeConfig {

    @Autowired
    private Environment environment;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(environment.getProperty("com.huotu.huobanplus.redis.host", "localhost"));
        factory.setPort(environment.getProperty("com.huotu.huobanplus.redis.port", Integer.TYPE, 6379));
        factory.setPassword(environment.getProperty("com.huotu.huobanplus.redis.password"));
        factory.setDatabase(environment.getProperty("com.huotu.huobanplus.redis.database", Integer.TYPE, 0));
        return factory;
    }
}

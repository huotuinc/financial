package com.huotu.financial.boot;

import org.luffy.lib.libspring.logging.LoggingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Administrator on 2016/8/28.
 */
@Configuration
@ImportResource("classpath:spring-jpa.xml")
@EnableJpaRepositories(value = "com.huotu.financial.repository")
@Import(value = {LoggingConfig.class})
public class SystemConfig {
}

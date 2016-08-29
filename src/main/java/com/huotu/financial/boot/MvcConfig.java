package com.huotu.financial.boot;

import com.huotu.financial.common.WebHandlerExceptionResolver;
import com.huotu.financial.common.WebInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Autowired
    private WebInterceptor webInterceptor;


    @Autowired
    private WebApplicationContext webApplicationContext;


    @Bean
    WebInterceptor webInterceptor() {
        return new WebInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(webInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/auth");

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**/*.html")
                .addResourceLocations("/html/", "/");
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new WebHandlerExceptionResolver());
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {

        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        SpringTemplateEngine engine = new SpringTemplateEngine();
        ServletContextTemplateResolver rootTemplateResolver = new ServletContextTemplateResolver(webApplicationContext.getServletContext());
        rootTemplateResolver.setPrefix("/");
        rootTemplateResolver.setSuffix(".html");
        rootTemplateResolver.setTemplateMode(TemplateMode.HTML);
        rootTemplateResolver.setCharacterEncoding("UTF-8");
        // start cache
        if(environment.acceptsProfiles("development")||environment.acceptsProfiles("develop")){
            rootTemplateResolver.setCacheable(false);
        }
        engine.setTemplateResolver(rootTemplateResolver);
        resolver.setContentType("text/html;charset=utf-8");
        resolver.setTemplateEngine(engine);
        resolver.setOrder(2147483647 + 10);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setExcludedViewNames(new String[]{
                "content/**"
        });
        return resolver;
    }
}

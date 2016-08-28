/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 表示该MVC 参数是微信openID，它注释的对象类型必须是{@link String}。
 * Created by jin on 2016/8/24.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OpenID {

    String value() default "";
}

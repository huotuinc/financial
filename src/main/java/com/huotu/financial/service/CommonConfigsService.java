/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface CommonConfigsService {

    /**
     * 网站地址
     *
     * @return
     */
    String getWebUrl();

    /**
     * 授权网站域名地址
     *
     * @return
     */
    String getAuthWebUrl();

    /**
     * 传输加密的密钥
     *
     * @return
     */
    String getAuthKeySecret();



    /**
     * appsecret用于mallapi
     *
     * @return
     */
    String getAppSecret();

    String getPicURL();

}

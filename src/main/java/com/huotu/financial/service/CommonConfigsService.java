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

    /**
     * 商城资源图片地址
     * @return
     */
    String getMallResourceServerUrl();

}

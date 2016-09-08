/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service;

import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.UserModel;

import java.io.IOException;

/**
 * Created by administrator on 2016/8/30.
 * 本地缓存
 */
public interface CacheService {

    /**
     * 根据用户id获取用户本地缓存信息
     *
     * @param userId 用户id
     * @return 用户信息
     * @throws IOException
     * @throws UserException 用户不存在异常
     */
    UserModel getOneByUserId(Long userId) throws IOException, UserException;

    /**
     * 清除用户本地缓存
     */
    void releaseUserMap();
}

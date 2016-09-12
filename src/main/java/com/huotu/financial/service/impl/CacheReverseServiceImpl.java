/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.financial.service.CacheReverseService;
import com.huotu.financial.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by administrator on 2016/9/12.
 */
@Service
public class CacheReverseServiceImpl implements CacheReverseService {

    @Autowired
    private CacheService cacheService;

    @Scheduled(cron = "0 0 0/10  * * ?")
    @Override
    public void reverseAll() throws IOException {
        cacheService.releaseUserMap();
    }
}

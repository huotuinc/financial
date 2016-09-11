/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service;

import org.springframework.ui.ModelMap;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * Created by administrator on 2016/8/30.
 */
public interface FinancialGoodsService {

    /**
     * 赎回理财操作
     *
     * @param no 理财编号
     * @return 赎回结果
     * @throws IOException
     */
    @Transactional
    ModelMap updateFlowStatus(String no) throws IOException;
}

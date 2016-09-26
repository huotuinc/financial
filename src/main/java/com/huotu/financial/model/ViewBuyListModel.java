/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/30.
 */
@Getter
@Setter
public class ViewBuyListModel {
    private String no;
    private String imageUrl;
    private String title;
    private BigDecimal price;
    private Integer amount;
    private BigDecimal moeny;

    private Date date;
    /**
     * 0 不可赎回 1 可赎回
     */
    private Integer status;

    /**
     * 对应{@link com.huotu.financial.enums.FinancialStatus}
     */
    private Integer ownerStatus;

}


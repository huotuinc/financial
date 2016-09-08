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
 * Created by administrator on 2016/8/30.
 */
@Getter
@Setter
public class BuyFlowModel {

    private String no;

    /**
     * 消费用户
     */
    private Long userId;

    /**
     * 微信名字
     */
    private String wxName;

    /**
     * 微信头像
     */
    private String wxImgURL;

    /**
     * 商品Id
     */
    private Long goodId;

    /**
     * 商户Id
     */
    private Long customerId;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 消费金额
     */
    private BigDecimal money;

    /**
     * 购买时间
     */
    private Date buyTime;

    /**
     * 理财商品名称
     */
    private String financialTitle;

    /***
     * 日利率
     * 如千分之8.2 则日利率值为8.2
     */
    private BigDecimal rate;

    /**
     * 赎回周期
     * 单位 天
     */
    private Integer redeemPeriod;

    /**
     * 理财状态
     */
    private Integer status;

    /**
     * 被抵用
     */
    private Boolean isUsed;

    /**
     * 一级上线用户
     * 首次购买设置此值
     */
    private Long belongOne;
}

/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.entity;

import com.huotu.financial.enums.FinancialStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户的理财商品
 * Created by Administrator on 2016/8/28.
 */

@Getter
@Setter
@Entity
@Table(name = "Mall_FinancialBuyFlow")
@Cacheable(value = false)
public class FinancialBuyFlow {

    /**
     * 理财编号
     * 时间(yyyyMMdd)+(8)随机
     */
    @Id
    @Column(length = 50)
    private String no;

    /**
     * 消费用户
     */
    private Long userId;

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
    @Column(precision = 20, scale = 2)
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 消费金额
     */
    @Column(precision = 20, scale = 2)
    private BigDecimal money;

    /**
     * 购买时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date buyTime;

    /**
     * 理财商品名称
     */
    @Column(length = 100)
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
    private FinancialStatus status;

    /**
     * 被抵用
     */
    private Boolean isUsed;

    /**
     * 一级上线用户
     * 首次购买设置此值
     */
    private Long belongOne;

    /**
     * 对应的外部订单号
     */
    @Column(length = 100)
    private String toOuterOrderNo;
}

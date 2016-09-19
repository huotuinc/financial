/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财收益
 * Created by Administrator on 2016/8/28.
 */

@Getter
@Setter
@Entity
@Table(name = "Mall_FinancialProfit")
@Cacheable(value = false)
public class FinancialProfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 理财编号
     */
    @Column(length = 50)
    private String no;

    /**
     * 消费用户
     */
    private Long userId;

    /**
     * 商户Id
     */
    private Long customerId;

    @Column(precision = 20, scale = 2)
    private BigDecimal money;

    /**
     * 时间
     */
    @Temporal(TemporalType.DATE)
    private Date time;
}

package com.huotu.financial.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财商品
 * Created by Administrator on 2016/8/28.
 */
@Getter
@Setter
@Entity
public class FinancialGoods {
    /**
     * 一一对应商城商品的Id
     */
    @Id
    private Long id;

    /**
     * 商户Id
     */
    private Long customerId;

    /**
     * 理财商品名称
     */
    @Column(length = 100)
    private String title;

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
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;


}

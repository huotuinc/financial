package com.huotu.financial.entity;

import com.huotu.financial.enums.FinancialStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财购买流水
 * Created by Administrator on 2016/8/28.
 */

@Getter
@Setter
@Entity
public class FinancialBuyFlow {

    /**
     * 理财编号
     * 时间(yyyyMMddhhmmssSSS)+userid
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
}

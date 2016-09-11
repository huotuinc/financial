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

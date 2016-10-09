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
import java.util.Date;

/**
 * Created by Administrator on 2016/9/22.
 * 退货退款流程
 */
@Entity
@Getter
@Setter
@Table(name = "Mall_FinancialReturnRefund")
@Cacheable(value = false)
public class FinancialReturnRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //退货地址
    private String address;

    private String phone;

//    private String

    //赎回原因
    private String reason;

    /**
     * 同意赎回时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date redemptionDate;

    /**
     * 申请时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyDate;

    //物流公司名字
    private String logisticalName;

    //物流单号
    private String logisticalCode;

    //物流手机号
    private String logisticalPhone;

    //预留字段，方便以后做流程
    //0：申请赎回，1：同意赎回，并发送地址，2：填写物流信息，提交，3：收到货并确定
    private Integer logisticalStatus;

    //备注信息
    private String description;
}

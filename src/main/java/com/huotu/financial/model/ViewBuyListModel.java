package com.huotu.financial.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

}


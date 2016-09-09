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
public class ViewProfitListModel {

    private  Long id;
    private String no;
    private Date date;
    private BigDecimal money;
}

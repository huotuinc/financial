package com.huotu.financial.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/8/30.
 */
@Getter
@Setter
public class ViewFinancialTotalModel {
    private BigDecimal money;
    private BigDecimal yesterdayIncome;
    private BigDecimal totalIncome;

}

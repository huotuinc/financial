package com.huotu.financial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/10.
 */
@Getter
@Setter
@AllArgsConstructor
public class ViewRedeemListModel {
    private String no;
    private String userName;
    private String financialTitle;
    private Date time;
}

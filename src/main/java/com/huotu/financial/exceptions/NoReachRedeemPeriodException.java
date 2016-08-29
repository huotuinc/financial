package com.huotu.financial.exceptions;

/**
 * 没到回购日期
 * Created by Administrator on 2016/8/29.
 */
public class NoReachRedeemPeriodException extends Exception {
    public NoReachRedeemPeriodException(String message) {
        super(message);
    }
}

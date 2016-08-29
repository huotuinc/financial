package com.huotu.financial.exceptions;

/**
 * 不是可回购状态
 * Created by Administrator on 2016/8/29.
 */
public class NoRedeemStatusException extends Exception {
    public NoRedeemStatusException(String message) {
        super(message);
    }
}

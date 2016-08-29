package com.huotu.financial.exceptions;

/**
 * 没找到可回购额度
 * Created by Administrator on 2016/8/29.
 */
public class NoFindRedeemAmountException extends Exception {
    public NoFindRedeemAmountException(String message) {
        super(message);
    }
}

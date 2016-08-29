package com.huotu.financial.common;

/**
 * Created by lgh on 2016/1/8.
 */
public class PublicParameterHolder {
    private static final ThreadLocal<PublicParameterModel> holder = new ThreadLocal();

    public static PublicParameterModel get() {
        return holder.get();
    }

    public static void set(PublicParameterModel model) {
        holder.set(model);
    }





}

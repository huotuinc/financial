/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.util.support;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

import java.io.Serializable;

/**
 * Created by administrator on 2016/8/29.
 */
@Immutable
@Getter
@Setter
public class BasicNameValuePair implements NameValuePair, Cloneable, Serializable {

    private final String name;

    private final Object value;

    public BasicNameValuePair(String name, Object value) {
        super();
        this.name = Args.notNull(name, "Name");
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return name;
        }
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.name);
        buffer.append("=");
        buffer.append(this.value);
        return buffer.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof NameValuePair) {
            final BasicNameValuePair that = (BasicNameValuePair) object;
            return this.name.equals(that.name)
                    && LangUtils.equals(this.value, that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.name);
        hash = LangUtils.hashCode(hash, this.value);
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

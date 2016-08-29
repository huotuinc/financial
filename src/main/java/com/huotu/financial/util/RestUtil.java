/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.util;

import com.huotu.financial.util.support.NameValuePair;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by administrator on 2016/8/29.
 */
public class RestUtil {

    public static ModelAndView success(String url, NameValuePair... nameValuePairs) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = toMap(nameValuePairs);
        view.addAllObjects(map);
        if (!StringUtils.isEmpty(url))
            view.setViewName(url);
        return view;
    }

    public static Map<String, Object> toMap(NameValuePair[] nameValuePairs) {
        List<NameValuePair> list = new ArrayList<>(Arrays.asList(nameValuePairs));
        Map<String, Object> map = new HashMap<>();
        list.stream().map(nameValuePair -> map.put(nameValuePair.getName(), nameValuePair.getValue()));
        return map;
    }
}

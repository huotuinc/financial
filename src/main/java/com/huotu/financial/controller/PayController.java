package com.huotu.financial.controller;


import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * 付款处理
 * Created by Administrator on 2016/9/10.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RequestMapping(value = "/pay")
@Controller
public class PayController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private FinancialBuyFlowService financialBuyFlowService;

    /**
     * 支付通知
     *
     * @param userid
     * @param orderid
     * @param unionorderid
     * @param timestamp
     * @param noncestr
     * @param sign
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.POST)
    @ResponseBody
    public String notice(Long userid, String orderid, String unionorderid, String timestamp, String noncestr, String sign) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userid.toString());
        map.put("orderid", orderid);
        map.put("unionorderid", unionorderid);
        map.put("timestamp", timestamp);
        map.put("noncestr", noncestr);
        map.put("sign", sign);
        String toSign = securityService.getMapSignByAppSecret(map);
        if (!toSign.equals(sign)) return "fail";

        financialBuyFlowService.handlePayNotice(userid, orderid);
        return "success";
    }
}

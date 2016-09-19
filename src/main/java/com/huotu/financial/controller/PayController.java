package com.huotu.financial.controller;


import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.SecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private static Log log = LogFactory.getLog(PayController.class);

    @Autowired
    private SecurityService securityService;
    @Autowired
    private FinancialBuyFlowService financialBuyFlowService;

    /**
     * 支付通知
     * //     *
     * //     * @param userid
     * //     * @param orderid
     * //     * @param unionorderid
     * //     * @param timestamp
     * //     * @param noncestr
     * //     * @param sign
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.POST)
    @ResponseBody
//    public String notice(@RequestParam(value = "userid") Long userid
//            , @RequestParam(value = "orderid") String orderid
//            , @RequestParam(value = "unionorderid") String unionorderid
//            , @RequestParam(value = "timestamp") String timestamp
//            , @RequestParam(value = "noncestr") String noncestr
//            , @RequestParam(value = "sign") String sign, HttpServletRequest request) throws UnsupportedEncodingException {
    public String notice(HttpServletRequest request) throws UnsupportedEncodingException {

        Long userid = Long.parseLong(request.getParameter("userid"));
        String orderid = request.getParameter("orderid");
        String unionorderid = request.getParameter("unionorderid");
        String timestamp = request.getParameter("timestamp");
        String noncestr = request.getParameter("noncestr");
        String sign = request.getParameter("sign");

//        log.info("useid:" + userid + " orderid:" + orderid + " unionorderid:" + unionorderid + " timestamp:" + timestamp + " noncestr:" + noncestr + " sign:" + sign);
        Map<String, String> map = new HashMap<>();
        map.put("userid", userid.toString());
        map.put("orderid", orderid);
        map.put("unionorderid", unionorderid);
        map.put("timestamp", timestamp);
        map.put("noncestr", noncestr);
        map.put("sign", sign);
        String toSign = securityService.getMapSignByAppSecret(map);
        if (!toSign.equals(sign)) {
            log.error("sign error");
            return "fail";
        }

        financialBuyFlowService.handlePayNotice(userid, orderid);
        return "success";
    }
}

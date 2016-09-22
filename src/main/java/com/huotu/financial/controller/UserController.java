package com.huotu.financial.controller;

import com.huotu.common.base.HttpHelper;
import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.exceptions.NoFindRedeemAmountException;
import com.huotu.financial.exceptions.NoReachRedeemPeriodException;
import com.huotu.financial.exceptions.NoRedeemStatusException;
import com.huotu.financial.model.ResultModel;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.SecurityService;
import com.huotu.financial.service.UserService;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/28.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CommonConfigsService commonConfigsService;

    @Autowired
    private UserService userService;

    @Autowired
    private FinancialBuyFlowService financialBuyFlowService;

    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;

    @RequestMapping("/auth")
    public String auth(String token, String sign, Integer code, String redirectUrl, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        log.debug("url:" + request.getRequestURL() + (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()));

        log.debug("enter auth " + token + " " + sign);
        //进行校验
        if (sign == null || !sign.equals(securityService.getSign(request))) {
            return "redirect:/html/error";
        }

        log.debug("auth sign passed");
        log.debug("redirectUrl:" + redirectUrl);

        if (code == 1) {
            //进行授权校验
            //生成sign
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            String toSign = securityService.getMapSign(map);
            //生成toUrl

            String toUrl = "";
            for (String key : map.keySet()) {
                toUrl += "&" + key + "=" + URLEncoder.encode(map.get(key), "utf-8");
            }

            toUrl = commonConfigsService.getAuthWebUrl() + "/api/check?" + (toUrl.length() > 0 ? toUrl.substring(1) : "");
            String responseText = HttpHelper.getRequest(toUrl + "&sign=" + toSign);

            log.debug(responseText);

            if (JsonPath.read(responseText, "$.resultCode").equals(1)) {
                Long userId = Long.parseLong(JsonPath.read(responseText, "$.resultData.data").toString());
                userService.setUserId(userId, request, response);
                log.debug("get userId and save in cookie");
                return "redirect:" + redirectUrl;
            }
        }

        log.info("auth error " + code);
        return "redirect:/html/error";
    }

    @RequestMapping(value = "/redeem", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel redeem(@RequestParam(value = "no") String no) {
        ResultModel resultModel = new ResultModel();

        FinancialBuyFlow financialBuyFlow = financialBuyFlowRepository.findOne(no);
        if (financialBuyFlow != null) {

            try {
                financialBuyFlowService.handleRedeem(financialBuyFlow);
            } catch (NoFindRedeemAmountException e) {
                resultModel.setCode("60001");
                resultModel.setMessage(e.getMessage());
                return resultModel;
            } catch (ParseException e) {
                resultModel.setCode("60002");
                resultModel.setMessage(e.getMessage());
                return resultModel;
            } catch (NoRedeemStatusException e) {
                resultModel.setCode("60003");
                resultModel.setMessage(e.getMessage());
                return resultModel;
            } catch (NoReachRedeemPeriodException e) {
                resultModel.setCode("60004");
                resultModel.setMessage(e.getMessage());
                return resultModel;
            }
        }

        resultModel.setCode("1");
        resultModel.setMessage("");
        return resultModel;
    }
}

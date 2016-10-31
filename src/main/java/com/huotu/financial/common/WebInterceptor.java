package com.huotu.financial.common;

import com.huotu.financial.model.AppPublicModel;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.SecurityService;
import com.huotu.financial.service.UserService;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.MerchantRepository;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/29.
 */
public class WebInterceptor implements HandlerInterceptor {
    private Log log = LogFactory.getLog(WebInterceptor.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CommonConfigsService commonConfigsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("url:" + request.getRequestURL() + (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()));
//        log.debug("enter interceptor");

        boolean gotoStatus = true;
        SystemEnvironment systemEnvironment = getSystemEnvironment(request);
        switch (systemEnvironment) {
            case WEIXIN:
                gotoStatus = gotoWeixin(request, response);
                break;
            case APP:
                log.debug("app container");
                gotoStatus = gotoApp(request, response);
                break;
            case UNKNOW:
                throw new Exception("not support ,please use weixin or app");
        }

        if (!gotoStatus) return false;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean gotoWeixin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = userService.getUserId(request);
        String paramUserId = request.getParameter("mainUserId");

        if (env.acceptsProfiles("production") || env.acceptsProfiles("staging")) {
            String customerIdStr = request.getParameter("customerId");
            if (customerIdStr == null) {
                customerIdStr = request.getParameter("customerid");
            }
            Long customerId = Long.parseLong(customerIdStr);
            Boolean toSSO = false;
            //强制刷新用户
            String forceRefresh = "0";
            log.debug("userId:" + (userId == null ? "" : userId.toString()) + " paramUserId:" + (paramUserId == null ? "" : paramUserId.toString()));
            //强制授权
            if (userId == null || userId == 0) {
                toSSO = true;
            } else if (!StringUtils.isEmpty(paramUserId) && !userId.toString().equals(paramUserId)) {
                //用户切换 强制刷新
                toSSO = true;
                forceRefresh = "1";
            } else {
                //商家切换 强制刷新
                User user = userRepository.findOne(userId);
                if (!user.getMerchant().getId().equals(customerId)) {
                    toSSO = true;
                    forceRefresh = "1";
                }

            }

            //进行单点登录
            if (toSSO) {
                //todo customerId为空
                String redirectUrl = commonConfigsService.getWebUrl() + "/user/auth?redirectUrl=" + URLEncoder.encode(request.getRequestURL()
                        + (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()), "utf-8");

                //生成sign
                Map<String, String> map = new HashMap<>();
                map.put("customerId", customerId.toString());
                map.put("redirectUrl", redirectUrl);
                map.put("forceRefresh", forceRefresh);
                String sign = securityService.getMapSign(map);

                //生成toUrl
                String toUrl = "";
                for (String key : map.keySet()) {
                    toUrl += "&" + key + "=" + URLEncoder.encode(map.get(key), "utf-8");
                }
                toUrl = commonConfigsService.getAuthWebUrl() + "/api/login?" + (toUrl.length() > 0 ? toUrl.substring(1) : "");

                response.sendRedirect(toUrl + "&sign=" + sign);
                return false;
            }
        }

        PublicParameterModel publicParameterModel = new PublicParameterModel();
        publicParameterModel.setUserId(userId);
        PublicParameterHolder.set(publicParameterModel);
        return true;
    }

    private boolean gotoApp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentRequestUrl = request.getRequestURL() + (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString());

        AppPublicModel appPublicModel = new AppPublicModel();
        String[] data = getAppHeaderInfo(request.getHeader("User-Agent"));
        if (data != null && data.length == 4) {
            appPublicModel.setMallUserId(Long.parseLong(data[1]));
            appPublicModel.setUnionId(data[2]);
            appPublicModel.setOpenId(data[3]);
            appPublicModel.setSign(data[0]);
        }
        if (!StringUtils.isEmpty(request.getParameter("customerId")))
            appPublicModel.setCustomerId(Long.parseLong(request.getParameter("customerId")));

        boolean checkPass = false;
        if (checkAppSign(appPublicModel)) {
            log.info("app auth check pass");
            PublicParameterModel publicParameterModel = new PublicParameterModel();
            publicParameterModel.setUserId(appPublicModel.getMallUserId());
            PublicParameterHolder.set(publicParameterModel);
            checkPass = true;
        }

        if (!checkPass) {
            log.info("app auth check no pass,begin to login page");
            StringBuilder url = new StringBuilder(getMerchantMainWebURL(appPublicModel.getCustomerId()));
            url.append("/UserCenter/Login.aspx").append("?").append("customerid=").append(appPublicModel.getCustomerId()).append("redirectURL=").append(URLEncoder.encode(currentRequestUrl, "utf-8"));
            response.sendRedirect(url.toString());
            return false;
        }
        return true;
    }

    /**
     * 获取系统环境
     *
     * @return
     */
    private SystemEnvironment getSystemEnvironment(HttpServletRequest request) {
        String header = request.getHeader("User-Agent").toLowerCase();
        if (header.indexOf("micromessenger") > 0) {
            return SystemEnvironment.WEIXIN;
        } else if (header.contains("mobile")) {
            return SystemEnvironment.APP;
        }
        return SystemEnvironment.UNKNOW;
    }


    private enum SystemEnvironment {
        WEIXIN,
        APP,
        UNKNOW
    }


    /**
     * 返回app信息
     *
     * @param userAgent 字符串
     * @return
     */
    public static String[] getAppHeaderInfo(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return null;
        }
        Pattern p = Pattern.compile(";hottec:([^;]+)");
        Matcher matcher = p.matcher(userAgent);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group(1));
        }
        return builder.toString().split(":");
    }


    /**
     * 判断签名是否正确
     *
     * @param webPublicModel
     * @return
     */
    public boolean checkAppSign(AppPublicModel webPublicModel) throws UnsupportedEncodingException {
        String s = webPublicModel.getMallUserId() + webPublicModel.getUnionId() + webPublicModel.getOpenId() + commonConfigsService.getAppUseSecret();
        String sign = DigestUtils.md5DigestAsHex(s.getBytes("UTF-8")).toLowerCase();
        return sign.equals(webPublicModel.getSign());
    }

    private String getMerchantMainWebURL(Long customerId) throws IOException {
        Merchant merchant = merchantRepository.findOne(customerId);
        String commonURL = getMerchantSubDomain(merchant.getSubDomain());
        return commonURL;
    }

    private String getMerchantSubDomain(String subDomain) {
        if (subDomain == null) {
            subDomain = "";
        }
        return "http://" + subDomain + "." + commonConfigsService.getMainDomain();
    }
}

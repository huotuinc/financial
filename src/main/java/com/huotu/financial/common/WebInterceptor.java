package com.huotu.financial.common;

import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.SecurityService;
import com.huotu.financial.service.UserService;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/29.
 */
public class WebInterceptor implements HandlerInterceptor {
    Log log = LogFactory.getLog(WebInterceptor.class);

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Long userId = userService.getUserId(request);
        String paramUserId = request.getParameter("mainUserId");

        log.debug("enter interceptor");

        if (!env.acceptsProfiles("develop")) {
            String customerIdStr = request.getParameter("customerId");
            if (customerIdStr == null) {
                customerIdStr = request.getParameter("customerid");
            }
            Long customerId = Long.parseLong(customerIdStr);
            Boolean toSSO = false;
            //强制刷新用户
            String forceRefresh = "0";
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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

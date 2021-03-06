/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.common.base.RSAHelper;
import com.huotu.financial.common.CookieHelper;
import com.huotu.financial.common.PublicParameterHolder;
import com.huotu.financial.common.PublicParameterModel;
import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.service.UserService;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/28.
 */
@Service
public class UserServiceImpl implements UserService {
    private static Log log = LogFactory.getLog(UserServiceImpl.class);

    private String userKey = "financialUserId";

    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK8Cd7gS6dOz3ALnICrlLOiGWv5RHgiQFvSsKMBudp59UDrsknSsaZsdBagrhMdtlKxYI1JzD2iJrGGBjPexvtGXVFrZjkLXZCdmeiM0L41m7VvkeI4ASD/4T3qxSjhMCRAVvJ0vC/sPffKR71In0hyUWMrFXCPR10zGUmcU9TwVAgMBAAECgYAeP1/vuZ0eUOTCv62onEmBus75S43UTwsYqLS2ZaEszV3TgVXiwnXSMFbs9PCTA1aB3w3jzy0nlTvs8lYp7VecWjG+rqayIZk2HtdKwNoEroqOPLgvDUTwxCC30CByZL4yb95XhNFBpd4p7cJLlPgf8M58WT7ttS3UquJDhYPYgQJBAPD8/HH07yMS17VfO6KwM7OCsnwUdrH3mGtK3ac86Z5xhelK4ikiKetu+1QFSeUOLm4Uv4K67c6lko+yPvmjqzUCQQC56VP0QhexQj4sylGtGSjqYGftRkkhbg6zHLURR0RMzTw+jXP8J6R0xTlIiBKJyK2xgAXuuqNUlyQVEJwCSolhAkEAmQ+F43c3P+ai3Q7MmMsjO1vCs25n6SciRts5JxRYKYtfC0rFlGyfhWpq9PWa9oHoWYCSFp1Vl4+wI9aJixM6FQJAA43vefsNgukWUTrpBts1Sg3fzsyKN2ZoR4pj99mZ97Hw1e1Ua1zCqyzeJIHdgN7iW0NsWZ0d5E8jdHel0/Fi4QJAJhb7Mt8pC5UCJNTJc1JQyzTUiZAvGr4EeTiAi0MCkPH85pOEr6ChMH4qI/51nTTqskoMJtd/xHxMDbEeXkrnAg==";
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvAne4EunTs9wC5yAq5Szohlr+UR4IkBb0rCjAbnaefVA67JJ0rGmbHQWoK4THbZSsWCNScw9oiaxhgYz3sb7Rl1Ra2Y5C12QnZnojNC+NZu1b5HiOAEg/+E96sUo4TAkQFbydLwv7D33yke9SJ9IclFjKxVwj0ddMxlJnFPU8FQIDAQAB";


    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long getUserId(HttpServletRequest request) throws Exception {
        if (env.acceptsProfiles("development")) {
//            userRepository.findAll();
            return 18165L;//146 4471商户 王明
//            return 96116L;
        } else {
            String encrypt = CookieHelper.get(request, userKey);
            log.debug("encypt:" + encrypt);
            try {
                Long userId = Long.parseLong(RSAHelper.decrypt(encrypt, privateKey));
                if (userId > 0) return userId;
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    public void setUserId(Long userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("setUserId:" + userId.toString());
        if (userId > 0) {
            log.debug("serverName:" + request.getServerName());
            String encrypt = RSAHelper.encrypt(userId.toString(), publicKey);
            CookieHelper.set(response, userKey, encrypt, request.getServerName(), 60 * 60 * 24 * 365);
        }

    }

    @Override
    public Long getUserFromSession() throws UserException {
        PublicParameterModel model = PublicParameterHolder.get();
        if (Objects.isNull(model) || Objects.isNull(model.getUserId()))
            throw new UserException("用户未登录");
        return model.getUserId();
    }
}

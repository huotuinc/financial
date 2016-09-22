package com.huotu.financial.controller;

import com.huotu.common.base.CookieHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/28.
 */
@Controller
@RequestMapping("/test")
public class AdminController {

    @RequestMapping("/setCookie")
    @ResponseBody
    public String setCookie(HttpServletRequest request, HttpServletResponse response) {
        String x = UUID.randomUUID().toString();
        CookieHelper.set(response, "a", x, request.getServerName(), 60 * 60 * 24 * 365);
        return "set " + x + " ok";
    }

    @RequestMapping("/getCookie")
    @ResponseBody
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
        String x = CookieHelper.get(request, "a");
        return "get " + x + " ok";
    }
}

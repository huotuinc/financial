package com.huotu.financial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/9/22.
 */
@Controller
@RequestMapping("/html")
public class HtmlController {
    @RequestMapping("/error")
    public String error(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "html/error";
    }
}

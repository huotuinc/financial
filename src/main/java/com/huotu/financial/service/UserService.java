package com.huotu.financial.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/8/28.
 */
public interface UserService {
    /**
     * 获取用户Id
     * 从加密的cookie中取出数据
     *
     * @param request
     * @return
     */
    Long getUserId(HttpServletRequest request) throws Exception;

    /**
     * 对用户的id进行加密
     * 放入cookie中
     */
    void setUserId(Long userId, HttpServletRequest request, HttpServletResponse response) throws Exception;
}

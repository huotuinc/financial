/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.service.impl;

import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.UserModel;
import com.huotu.financial.service.CacheService;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by administrator on 2016/8/30.
 */
@Service
public class CacheServiceImpl implements CacheService {
    private static Map<Long, UserModel> userMap;

    static {
        userMap = new ConcurrentHashMap<>();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel getOneByUserId(Long userId) throws IOException, UserException {
        if (userMap.containsKey(userId))
            return userMap.get(userId);
        User user = userRepository.findOne(userId);
        if (Objects.isNull(user))
            throw new UserException("用户不存在");
        UserModel model = new UserModel();
        model.setUserId(user.getId());
        model.setWxName(user.getWxNickName());
        model.setWxImgURL(user.getWeixinImageUrl());
        userMap.put(userId, model);
        return model;
    }

    public void releaseUserMap() {
        userMap.clear();
    }
}

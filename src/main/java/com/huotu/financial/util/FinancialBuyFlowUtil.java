///*
// * 版权所有:杭州火图科技有限公司
// * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
// *
// * (c) Copyright Hangzhou Hot Technology Co., Ltd.
// * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
// * 2013-2016. All rights reserved.
// */
//
//package com.huotu.financial.util;
//
//import com.huotu.financial.entity.FinancialBuyFlow;
//import org.springframework.cglib.beans.BeanCopier;
//
///**
// * Created by administrator on 2016/9/11.
// */
//public class FinancialBuyFlowUtil {
//
//    private static BeanCopier copier = BeanCopier.create(FinancialBuyFlow.class, FinancialBuyFlowModel.class, false);
//
//    public static FinancialBuyFlowModel copyDomainToModel(FinancialBuyFlow item) {
//        FinancialBuyFlowModel model = new FinancialBuyFlowModel();
//        copier.copy(item, model, null);
//        return model;
//    }
//}

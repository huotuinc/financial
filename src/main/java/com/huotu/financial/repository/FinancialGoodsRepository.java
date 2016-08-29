/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.repository;

import com.huotu.financial.entity.FinancialGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface FinancialGoodsRepository extends JpaRepository<FinancialGoods, Long>,
        JpaSpecificationExecutor<FinancialGoods> {

    Page<FinancialGoods> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}

/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.repository;

import com.huotu.financial.entity.FinancialProfit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface FinancialProfitRepository extends JpaRepository<FinancialProfit, Long> {

    /**
     * 根据用户id和理财编号查询每日流水
     *
     * @param no       理财产品编号
     * @param pageable 分页
     * @return 每日流水列表
     */
    Page<FinancialProfit> findAllByNo(@Param("no") String no, Pageable pageable);

    @Query("select sum(profit.money) from FinancialProfit profit where profit.userId=?1 and profit.time>=?2 and profit.time<?3")
    BigDecimal countYestodayProfit(Long userId, Date start, Date end);

    @Query("select sum(profit.money) from FinancialProfit profit where profit.userId=?1")
    BigDecimal countTotalProfit(Long userId);


}

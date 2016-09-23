/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.repository;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface FinancialBuyFlowRepository extends JpaRepository<FinancialBuyFlow, String>, JpaSpecificationExecutor<FinancialBuyFlow> {
    List<FinancialBuyFlow> findAllByStatus(FinancialStatus status);

    @Query("select flow from FinancialBuyFlow flow where flow.status<>?1")
    List<FinancialBuyFlow> findAllCanProfit(FinancialStatus status);

//    List<FinancialBuyFlow> findAllByUserIdAndIsUsedAndBuyTimeGreaterThanAndMoneyGreaterThanEqual(Long userId, Boolean isUsed, Date buyTime, BigDecimal money);
//
//    List<FinancialBuyFlow> findAllByBelongOneAndIsUsedAndMoneyGreaterThanEqual(Long belongOne, Boolean isUsed, BigDecimal money);


    @Query("select flow from FinancialBuyFlow flow where (flow.userId=?1 and flow.isUsed=false  and flow.buyTime>?2 and flow.money>=?3 ) or (flow.belongOne=?1 and flow.isUsed=false and flow.money>=?3) order by flow.money,flow.buyTime")
    List<FinancialBuyFlow> findAllForRedeem(Long userId, Date buyTime, BigDecimal money);

    /**
     * 根据商户id查询用户理财列表
     *
     * @param customerId 商户id
     * @param goodId     商品id
     * @param pageable   分页
     * @return 用户理财列表
     * @deprecated 使用 {@link com.huotu.financial.service.FinancialBuyFlowService#findAllByCustomerIdAndGoodIdAndNo(Long, Long, String, Pageable)}
     */
    Page<FinancialBuyFlow> findAllByCustomerIdAndGoodId(@Param("customerId") Long customerId,
                                                        @Param("goodId") Long goodId, Pageable pageable);


    /**
     * 获取理财金额
     *
     * @param userId
     * @param status
     * @return
     */
    @Query("select sum(flow.money) from FinancialBuyFlow flow where flow.userId=?1 and flow.status<>?2")
    BigDecimal countFinancialMoney(Long userId, FinancialStatus status);

    /**
     * 获取某状态下理财总金额
     *
     * @param goodsId 商品id
     * @param status  状态
     * @return 总金额
     */
    @Query("select sum(flow.money) from FinancialBuyFlow flow where flow.goodId=?1 and flow.status=?2")
    BigDecimal sumMoneyByGoodIdAndStatus(Long goodsId, FinancialStatus status);

    /**
     * 获取购买某理财商品的人数
     *
     * @param goodsId 商品id
     * @return
     */
    @Query("select count(distinct flow.userId) from FinancialBuyFlow flow where flow.goodId=?1")
    Long countByGoodsId(Long goodsId);
}

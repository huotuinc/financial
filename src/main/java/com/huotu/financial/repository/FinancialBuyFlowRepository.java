package com.huotu.financial.repository;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.enums.FinancialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface FinancialBuyFlowRepository extends JpaRepository<FinancialBuyFlow, String> {
    List<FinancialBuyFlow> findAllByStatus(FinancialStatus status);

//    List<FinancialBuyFlow> findAllByUserIdAndIsUsedAndBuyTimeGreaterThanAndMoneyGreaterThanEqual(Long userId, Boolean isUsed, Date buyTime, BigDecimal money);
//
//    List<FinancialBuyFlow> findAllByBelongOneAndIsUsedAndMoneyGreaterThanEqual(Long belongOne, Boolean isUsed, BigDecimal money);


    @Query("select flow from FinancialBuyFlow flow where (flow.userId=?1 and flow.isUsed=false  and flow.buyTime>?2 and flow.money>=?3 ) or (flow.belongOne=?1 and flow.isUsed=false and flow.money>=?3) order by flow.money,flow.buyTime")
    List<FinancialBuyFlow> findAllForRedeem(Long userId, Date buyTime, BigDecimal money);

}

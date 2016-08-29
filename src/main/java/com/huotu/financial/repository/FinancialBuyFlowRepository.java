package com.huotu.financial.repository;

import com.huotu.financial.entity.FinancialBuyFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface FinancialBuyFlowRepository extends JpaRepository<FinancialBuyFlow,String> {
}

package com.huotu.financial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/28.
 */
@Repository
public interface UserFlowRepository extends JpaRepository<UserFlow, Long> {
}

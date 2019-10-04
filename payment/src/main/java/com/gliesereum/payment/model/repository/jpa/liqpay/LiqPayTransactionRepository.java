package com.gliesereum.payment.model.repository.jpa.liqpay;

import com.gliesereum.payment.model.liqpay.LiqPayTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LiqPayTransactionRepository extends JpaRepository<LiqPayTransactionEntity, UUID> {

    @Query("SELECT t FROM LiqPayTransactionEntity t WHERE t.order_id = :orderId")
    LiqPayTransactionEntity getByOrderId(@Param("orderId") UUID orderId);
}
package com.gliesereum.payment.model.repository.jpa.liqpay;

import com.gliesereum.payment.model.liqpay.LiqPayTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LiqPayTransactionRepository extends JpaRepository<LiqPayTransactionEntity, UUID> {

    LiqPayTransactionEntity getByOrder_id(UUID orderId);
}
package com.gliesereum.payment.model.repository.jpa.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpOrderTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WfpOrderTransactionRepository extends JpaRepository<WfpOrderTransactionEntity, UUID> {

    List<WfpOrderTransactionEntity> getByOrderId(UUID orderId);
}
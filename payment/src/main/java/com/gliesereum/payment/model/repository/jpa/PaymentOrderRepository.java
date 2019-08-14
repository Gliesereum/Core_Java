package com.gliesereum.payment.model.repository.jpa;

import com.gliesereum.payment.model.entity.PaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentOrderRepository extends JpaRepository<PaymentOrderEntity, UUID> {
}
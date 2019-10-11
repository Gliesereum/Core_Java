package com.gliesereum.payment.model.repository.jpa.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WfpOrderRepository extends JpaRepository<WfpOrderEntity, UUID> {

    WfpOrderEntity getByOrderReference(UUID orderId);
}
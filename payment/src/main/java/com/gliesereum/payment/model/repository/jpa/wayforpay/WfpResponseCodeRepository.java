package com.gliesereum.payment.model.repository.jpa.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpResponseCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @since 9/5/19
 */
public interface WfpResponseCodeRepository extends JpaRepository<WfpResponseCodeEntity, UUID> {

    WfpResponseCodeEntity getByReasonCode(Integer code);
}
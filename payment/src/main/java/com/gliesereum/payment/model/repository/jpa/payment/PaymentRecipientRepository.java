package com.gliesereum.payment.model.repository.jpa.payment;

import com.gliesereum.payment.model.entity.payment.PaymentRecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentRecipientRepository extends JpaRepository<PaymentRecipientEntity, UUID> {

    PaymentRecipientEntity getByPublicKey(String publicKey);

    PaymentRecipientEntity getByObjectId(UUID objectId);
}

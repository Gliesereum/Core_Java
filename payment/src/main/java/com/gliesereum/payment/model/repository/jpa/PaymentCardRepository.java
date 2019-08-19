package com.gliesereum.payment.model.repository.jpa;

import com.gliesereum.payment.model.entity.PaymentCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentCardRepository extends JpaRepository<PaymentCardEntity, UUID> {

    List<PaymentCardEntity> getByOwnerId(UUID ownerId);

    PaymentCardEntity getById(UUID id);

}

package com.gliesereum.payment.model.repository.jpa;

import com.gliesereum.payment.model.entity.PaymentSenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentSenderRepository extends JpaRepository<PaymentSenderEntity, UUID> {

    List<PaymentSenderEntity> getByObjectId(UUID objectId);

    //PaymentSenderEntity getByObjectIdAndFavorite(UUID objectId, boolean isFavorite);
}

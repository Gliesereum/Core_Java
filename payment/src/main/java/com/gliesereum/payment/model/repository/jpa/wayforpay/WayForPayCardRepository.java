package com.gliesereum.payment.model.repository.jpa.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WayForPayCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WayForPayCardRepository extends JpaRepository<WayForPayCardEntity, UUID> {

    List<WayForPayCardEntity> getByOwnerId(UUID ownerId);

    WayForPayCardEntity getById(UUID id);

}

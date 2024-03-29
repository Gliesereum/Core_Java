package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.BorrowerOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface BorrowerOfferRepository extends JpaRepository<BorrowerOfferEntity, UUID> {

    List<BorrowerOfferEntity> findAllByStateType(OfferStateType stateType);

    List<BorrowerOfferEntity> findAllByCustomerIdOrderByCreate(UUID userId);
}
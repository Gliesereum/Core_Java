package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface InvestorOfferRepository extends JpaRepository<InvestorOfferEntity, UUID> {

    List<InvestorOfferEntity> findAllByStateType(OfferStateType stateType);

    List<InvestorOfferEntity> findAllByArtBondId(UUID artBondId);

    List<InvestorOfferEntity> findAllByCustomerIdOrderByCreate(UUID customerId);

}
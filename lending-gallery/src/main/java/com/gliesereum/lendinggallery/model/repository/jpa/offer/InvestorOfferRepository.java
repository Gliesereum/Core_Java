package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface InvestorOfferRepository extends JpaRepository<InvestorOfferEntity, UUID>, CustomizedInvestorRepository {

    List<InvestorOfferEntity> findAllByStateType(OfferStateType stateType);

    List<InvestorOfferEntity> findAllByStateTypeIn(List<OfferStateType> states);

    List<InvestorOfferEntity> findAllByArtBondId(UUID artBondId);

    List<InvestorOfferEntity> findAllByArtBondIdAndStateType(UUID artBondId, OfferStateType stateType);

    List<InvestorOfferEntity> findAllByCustomerIdOrderByCreate(UUID customerId);
    
    Page<InvestorOfferEntity> findAllByCustomerIdOrderByCreate(UUID customerId, Pageable pageable);

    List<InvestorOfferEntity> findAllByArtBondIdAndCustomerIdOrderByCreate(UUID artBondId, UUID customerId);

}

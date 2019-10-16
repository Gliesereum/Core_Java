package com.gliesereum.lendinggallery.service.offer;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferFullModelDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OfferSearchDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface InvestorOfferService extends DefaultService<InvestorOfferDto, InvestorOfferEntity> {

    List<InvestorOfferDto> getAllByState(OfferStateType state);

    InvestorOfferDto updateState(OfferStateType state, UUID id, String comment);

    List<InvestorOfferDto> getAllByArtBond(UUID id);

    List<InvestorOfferDto> getAllByArtBondAndStateType(UUID id, OfferStateType stateType);

    List<InvestorOfferDto> getAllByUser();

    List<InvestorOfferDto> getAllByArtBondAndCurrentUser(UUID artBondId);

    List<InvestorOfferFullModelDto> getAllFullModelByState(OfferStateType state);

    List<InvestorOfferFullModelDto> searchInvestorOffersFullModelByCurrentAdviser(OfferSearchDto search);

    InvestorOfferDto setComment(UUID id, String comment);
}
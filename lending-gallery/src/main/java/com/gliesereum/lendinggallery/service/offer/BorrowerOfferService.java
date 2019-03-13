package com.gliesereum.lendinggallery.service.offer;

import com.gliesereum.lendinggallery.model.entity.offer.BorrowerOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.BorrowerOfferDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface BorrowerOfferService extends DefaultService<BorrowerOfferDto, BorrowerOfferEntity> {

    List<BorrowerOfferDto> getAllByState(OfferStateType state);

    BorrowerOfferDto updateState(OfferStateType state, UUID id);

    List<BorrowerOfferDto> getAllByUser();
}
package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OfferSearchDto;

import java.util.List;

public interface CustomizedInvestorRepository {

    List<InvestorOfferEntity> searchInvestorOffersByParams(OfferSearchDto search);
}

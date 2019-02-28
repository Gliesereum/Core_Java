package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.InvestorOfferRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class InvestorOfferServiceImpl extends DefaultServiceImpl<InvestorOfferDto, InvestorOfferEntity> implements InvestorOfferService {

    @Autowired
    private InvestorOfferRepository repository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ArtBondService artBondService;

    private static final Class<InvestorOfferDto> DTO_CLASS = InvestorOfferDto.class;
    private static final Class<InvestorOfferEntity> ENTITY_CLASS = InvestorOfferEntity.class;

    public InvestorOfferServiceImpl(InvestorOfferRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<InvestorOfferDto> getAllByState(OfferStateType state) {
        List<InvestorOfferEntity> entities = repository.findAllByStateType(state);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public InvestorOfferDto updateState(OfferStateType state, UUID id) {
        if (state == null) {
            throw new ClientException(OFFER_STATE_IS_EMPTY);
        }
        InvestorOfferDto result = findById(id);
        result.setStateType(state);
        return super.update(result);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBond(UUID id) {
        List<InvestorOfferEntity> entities = repository.findAllByArtBondId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public InvestorOfferDto create(InvestorOfferDto dto) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        checkModel(dto);
        CustomerDto customer = customerService.findByUserId(SecurityUtil.getUserId());
        dto.setCustomerId(customer.getId());
        return super.create(dto);
    }

    @Override
    public InvestorOfferDto update(InvestorOfferDto dto) {
        checkModel(dto);
        InvestorOfferDto saveDto = findById(dto.getId());
        dto.setCustomerId(saveDto.getCustomerId());
        dto.setStateType(saveDto.getStateType());
        return super.update(dto);
    }

    private InvestorOfferDto findById(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        InvestorOfferDto result = getById(id);
        if (result == null) {
            throw new ClientException(OFFER_NOT_FOUND_BY_ID);
        }
        return result;
    }

    private void checkModel(InvestorOfferDto dto) {
        if (dto == null) {
            throw new ClientException(MODEL_IS_EMPTY);
        }
        ArtBondDto artBond = artBondService.getById(dto.getArtBondId());
        if (artBond == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        if (!artBond.getStatusType().equals(StatusType.ACTIVE_COLLECTION) ||
                artBond.getSpecialStatusType().equals(SpecialStatusType.BLOCKED)) {
            throw new ClientException(ART_BOND_NOT_AVAILABLE_FOR_INVESTMENT);
        }
        List<InvestorOfferDto> offers = getAllByArtBond(artBond.getId());
        int commonSum = 0;
        if (CollectionUtils.isNotEmpty(offers)) {
            commonSum = offers.stream().mapToInt(InvestorOfferDto::getSumInvestment).sum();
        }
        if(dto.getSumInvestment() == null || dto.getSumInvestment() == 0){
            throw new ClientException(SUM_OF_INVESTMENT_CAN_NOT_BE_ZERO);
        }
        if ((artBond.getPrice() - commonSum) < dto.getSumInvestment()) {
            throw new ClientException(SUM_EXCEEDS_AMOUNT_ALLOWED_FOR_INVESTMENT);
        }
    }
}

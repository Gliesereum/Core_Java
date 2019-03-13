package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.BorrowerOfferEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.BorrowerOfferRepository;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.BorrowerOfferService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.BorrowerOfferDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
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
public class BorrowerOfferServiceImpl extends DefaultServiceImpl<BorrowerOfferDto, BorrowerOfferEntity> implements BorrowerOfferService {

    @Autowired
    private BorrowerOfferRepository repository;

    @Autowired
    private CustomerService customerService;

    private static final Class<BorrowerOfferDto> DTO_CLASS = BorrowerOfferDto.class;
    private static final Class<BorrowerOfferEntity> ENTITY_CLASS = BorrowerOfferEntity.class;

    public BorrowerOfferServiceImpl(BorrowerOfferRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<BorrowerOfferDto> getAllByState(OfferStateType state) {
        List<BorrowerOfferEntity> entities = repository.findAllByStateType(state);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public BorrowerOfferDto updateState(OfferStateType state, UUID id) {
        if(state == null){
            throw new ClientException(OFFER_STATE_IS_EMPTY);
        }
        BorrowerOfferDto result = findById(id);
        result.setStateType(state);
        return super.update(result);
    }

    @Override
    public List<BorrowerOfferDto> getAllByUser() {
        List<BorrowerOfferEntity> entities = repository.findAllByCustomerIdOrderByCreate(getCustomer().getUserId());
        return converter.convert(entities, dtoClass);
    }

    @Override
    public BorrowerOfferDto create(BorrowerOfferDto dto) {
        if (dto == null) {
            throw new ClientException(MODEL_IS_EMPTY);
        }
        dto.setCustomerId(getCustomer().getId());
        return super.create(dto);
    }

    private BorrowerOfferDto findById(UUID id){
        if(id == null){
            throw new ClientException(ID_IS_EMPTY);
        }
        BorrowerOfferDto result = getById(id);
        if(result == null){
            throw new ClientException(OFFER_NOT_FOUND_BY_ID);
        }
        return result;
    }

    private CustomerDto getCustomer(){
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.findByUserId(SecurityUtil.getUserId());
    }

}

package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.InvestorOfferRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferFullModelDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private static final Class<InvestorOfferDto> DTO_CLASS = InvestorOfferDto.class;
    private static final Class<InvestorOfferEntity> ENTITY_CLASS = InvestorOfferEntity.class;

    private final InvestorOfferRepository investorOfferRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ArtBondService artBondService;

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private UserExchangeService userExchangeService;

    public InvestorOfferServiceImpl(InvestorOfferRepository investorOfferRepository, DefaultConverter defaultConverter) {
        super(investorOfferRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.investorOfferRepository = investorOfferRepository;
    }

    @Override
    public List<InvestorOfferDto> getAllByState(OfferStateType state) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByStateType(state);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public InvestorOfferDto updateState(OfferStateType state, UUID id) {
        if (state == null) {
            throw new ClientException(OFFER_STATE_IS_EMPTY);
        }
        InvestorOfferDto result = findById(id);
        checkUpdateState(result, state);
        result.setStateType(state);
        result = super.update(result);
        if ((result != null) && state.equals(OfferStateType.COMPLETED)) {
            operationsStoryService.create(
                    new OperationsStoryDto(result.getCustomerId(),
                            result.getArtBondId(),
                            result.getSumInvestment().doubleValue(),
                            result.getStockCount(),
                            OperationType.PURCHASE.name().toLowerCase(),
                            OperationType.PURCHASE.name().toLowerCase(),
                            LocalDateTime.now(),
                            OperationType.PURCHASE));

        }
        return result;
    }

    private void checkUpdateState(InvestorOfferDto offer, OfferStateType state) {
        if (offer == null) {
            throw new ClientException(OFFER_NOT_FOUND_BY_ID);
        }
        ArtBondDto artBond = artBondService.getById(offer.getArtBondId());
        Integer commonSum = 0;

        if (state.equals(OfferStateType.COMPLETED)) {

            List<InvestorOfferDto> offers = getAllByArtBondAndStateType(artBond.getId(), OfferStateType.COMPLETED);

            if (CollectionUtils.isNotEmpty(offers)) {
                commonSum = offers.stream().mapToInt(InvestorOfferDto::getSumInvestment).sum();
            }
            if ((artBond.getPrice() - commonSum) < offer.getSumInvestment()) {
                throw new ClientException(SUM_EXCEEDS_AMOUNT_ALLOWED_FOR_INVESTMENT);
            }
        }
        if (artBond.getPrice() == (commonSum + offer.getSumInvestment())) {
            artBond.setStatusType(StatusType.COMPLETED_COLLECTION);
            artBondService.superUpdateArtBond(artBond);
            List<InvestorOfferDto> offers = getAllByArtBond(artBond.getId());
            if (CollectionUtils.isNotEmpty(offers)) {
                List<InvestorOfferDto> refused = new ArrayList<>();
                offers.forEach(f -> {
                    if (!f.getStateType().equals(OfferStateType.COMPLETED)) {
                        f.setStateType(OfferStateType.REFUSED);
                        refused.add(f);
                    }
                });
                super.update(refused);
            }
        }
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBond(UUID id) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByArtBondId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBondAndStateType(UUID id, OfferStateType stateType) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByArtBondIdAndStateType(id, stateType);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByUser() {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = investorOfferRepository.findAllByCustomerIdOrderByCreate(customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBondAndCurrentUser(UUID artBondId) {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = investorOfferRepository.findAllByArtBondIdAndCustomerIdOrderByCreate(artBondId, customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public InvestorOfferDto create(InvestorOfferDto dto) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        checkModel(dto);
        CustomerDto customer = getCustomer();
        if (customer == null) {
            throw new ClientException(CUSTOMER_NOT_FOUND_BY_USER_ID);
        }
        dto.setCustomerId(customer.getId());
        dto.setCreate(LocalDateTime.now());
        dto.setStateType(OfferStateType.REQUEST);
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

    @Override
    public List<InvestorOfferFullModelDto> getAllFullModelByState(OfferStateType state) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByStateType(state);
        List<InvestorOfferFullModelDto> result = converter.convert(entities, InvestorOfferFullModelDto.class);
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(i -> {
                UUID artBondId = i.getArtBondId();
                if (artBondId != null) {
                    i.setArtBond(artBondService.getById(artBondId));
                    i.getArtBond().setAmountCollected(artBondService.getAmountCollected(artBondId));
                }
            });

            customerService.setCustomerAndUser(result, InvestorOfferFullModelDto::getCustomerId,
                    InvestorOfferFullModelDto::setCustomer, InvestorOfferFullModelDto::setUser);
        }
        return result;
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
        if (dto.getStockCount() == null || dto.getStockCount() == 0) {
            throw new ClientException(SUM_OF_INVESTMENT_CAN_NOT_BE_ZERO);
        }

        List<InvestorOfferDto> offers = getAllByArtBondAndStateType(artBond.getId(), OfferStateType.COMPLETED);
        Integer commonSum = 0;
        if (CollectionUtils.isNotEmpty(offers)) {
            commonSum = offers.stream().mapToInt(InvestorOfferDto::getSumInvestment).sum();
        }

        Double sumInvesting = artBond.getStockPrice() * dto.getStockCount();

        if ((artBond.getPrice() - commonSum) < sumInvesting) {
            throw new ClientException(SUM_EXCEEDS_AMOUNT_ALLOWED_FOR_INVESTMENT);
        }

        dto.setSumInvestment(sumInvesting.intValue());
    }

    private CustomerDto getCustomer() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.findByUserId(SecurityUtil.getUserId());
    }
}

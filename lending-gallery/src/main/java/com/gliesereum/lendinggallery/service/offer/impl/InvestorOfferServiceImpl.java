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
import com.gliesereum.share.common.model.dto.account.user.UserDto;
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
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private UserExchangeService userExchangeService;

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
        result = super.update(result);
        if (result != null) {
            if (state.equals(OfferStateType.COMPLETED)) {
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
        }
        return result;
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBond(UUID id) {
        List<InvestorOfferEntity> entities = repository.findAllByArtBondId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByUser() {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = repository.findAllByCustomerIdOrderByCreate(customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBondAndCurrentUser(UUID artBondId) {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = repository.findAllByArtBondIdAndCustomerIdOrderByCreate(artBondId, customer.getId());
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
        List<InvestorOfferEntity> entities = repository.findAllByStateType(state);
        List<InvestorOfferFullModelDto> result = converter.convert(entities, InvestorOfferFullModelDto.class);
        if(CollectionUtils.isNotEmpty(result)) {
            List<UUID> userIds = new ArrayList<>();
            result.forEach(i -> {
                UUID customerId = i.getCustomerId();
                if (customerId != null) {
                    CustomerDto customer = customerService.getById(customerId);
                    if (customer != null) {
                        i.setCustomer(customer);
                        UUID userId = customer.getUserId();
                        if (userId != null) {
                            userIds.add(userId);
                        }
                    }
                }
                UUID artBondId = i.getArtBondId();
                if (artBondId != null) {
                    i.setArtBond(artBondService.getById(artBondId));
                }
            });
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<UserDto> users = userExchangeService.findByIds(userIds);
                if (CollectionUtils.isNotEmpty(users)) {
                    Map<UUID, UserDto> userMap = users.stream().collect(Collectors.toMap(UserDto::getId, i -> i));
                    result.forEach(i -> {
                        if ((i.getCustomer() != null) && (i.getCustomer().getUserId() != null)) {
                            i.setUser(userMap.get(i.getCustomer().getUserId()));
                        }
                    });
                }
            }
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
        List<InvestorOfferDto> offers = getAllByArtBond(artBond.getId());
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

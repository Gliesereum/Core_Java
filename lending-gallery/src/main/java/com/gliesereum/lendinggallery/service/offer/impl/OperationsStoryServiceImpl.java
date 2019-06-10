package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.OperationsStoryRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ART_BOND_NOT_FOUND_BY_ID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class OperationsStoryServiceImpl extends DefaultServiceImpl<OperationsStoryDto, OperationsStoryEntity> implements OperationsStoryService {

    private static final Class<OperationsStoryDto> DTO_CLASS = OperationsStoryDto.class;
    private static final Class<OperationsStoryEntity> ENTITY_CLASS = OperationsStoryEntity.class;

    private final OperationsStoryRepository operationsStoryRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ArtBondService artBondService;

    public OperationsStoryServiceImpl(OperationsStoryRepository operationsStoryRepository, DefaultConverter defaultConverter) {
        super(operationsStoryRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.operationsStoryRepository = operationsStoryRepository;
    }

    @Override
    public List<OperationsStoryDto> getAllByCustomerId(UUID customerId) {
        List<OperationsStoryEntity> entities = operationsStoryRepository.findAllByCustomerIdOrderByCreate(customerId);
        List<OperationsStoryDto> result = converter.convert(entities, dtoClass);
        if(CollectionUtils.isNotEmpty(result)){
           setArtBond(result);
        }
        return result;
    }

    @Override
    public Map<UUID, List<OperationsStoryDto>> getAllByCustomerIds(List<UUID> customerIds) {
        Map<UUID, List<OperationsStoryDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(customerIds)) {
            List<OperationsStoryEntity> entities = operationsStoryRepository.findAllByCustomerIdIn(customerIds);
            List<OperationsStoryDto> dtos = converter.convert(entities, dtoClass);
            if (CollectionUtils.isNotEmpty(dtos)) {
                setArtBond(dtos);
                dtos.forEach(i -> {
                    List<OperationsStoryDto> value = result.getOrDefault(i.getCustomerId(), new ArrayList<>());
                    value.add(i);
                    result.put(i.getCustomerId(), value);
                });
            }
        }
        return result;
    }

    @Override
    public List<OperationsStoryDto> getAllByCustomerIdAndArtBondId(UUID customerId, UUID artBondId) {
        List<OperationsStoryEntity> entities = operationsStoryRepository.findAllByCustomerIdAndArtBondIdOrderByCreate(customerId, artBondId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<OperationsStoryDto> getAllByUserId(UUID userId) {
        CustomerDto customer = getCustomer();
        List<OperationsStoryEntity> entities = null;
        if (customer != null) {
            entities = operationsStoryRepository.findAllByCustomerIdOrderByCreate(customer.getId());
        }
        List<OperationsStoryDto> result = converter.convert(entities, dtoClass);
        if(CollectionUtils.isNotEmpty(result)){
            setArtBond(result);
        }
        return result;
    }

    @Override
    public List<OperationsStoryDto> getAllForUserByArtBondId(UUID artBondId) {
        if(artBondId == null || !artBondService.isExist(artBondId)){
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        CustomerDto customer = getCustomer();
        List<OperationsStoryEntity> entities = null;
        if (customer != null) {
            entities = operationsStoryRepository.findAllByCustomerIdAndArtBondIdOrderByCreate(customer.getId(), artBondId);
        }
        List<OperationsStoryDto> result = converter.convert(entities, dtoClass);
        if(CollectionUtils.isNotEmpty(result)){
            setArtBond(result);
        }
        return result;
    }

    @Override
    @Transactional
    public OperationsStoryDto create(OperationsStoryDto dto) {
        OperationsStoryDto result = super.create(dto);
        setArtBond(result);
        return result;
    }

    @Override
    @Transactional
    public OperationsStoryDto update(OperationsStoryDto dto) {
        OperationsStoryDto result = super.update(dto);
        setArtBond(result);
        return result;
    }

    @Override
    public OperationsStoryDto getById(UUID id) {
        OperationsStoryDto result = super.getById(id);
        setArtBond(result);
        return result;
    }

    @Override
    public List<OperationsStoryDto> getAllByCustomerIdAndOperationType(UUID customerId, OperationType operationType) {
        List<OperationsStoryDto> result = null;
        if (ObjectUtils.allNotNull(customerId, operationType)) {
            List<OperationsStoryEntity> entities = operationsStoryRepository.findAllByCustomerIdAndOperationType(customerId, operationType);
            result = converter.convert(entities, dtoClass);
        }
        if(CollectionUtils.isNotEmpty(result)){
            setArtBond(result);
        }
        return result;
    }

    @Override
    public List<OperationsStoryDto> getAllPurchaseByArtBond(UUID artBondId) {
        List<OperationsStoryDto> result = null;
        if(artBondId != null) {
            List<OperationsStoryEntity> entities = operationsStoryRepository.findAllByArtBondIdAndOperationType(artBondId, OperationType.PURCHASE);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = converter.convert(entities, dtoClass);
            }
        }
        if(CollectionUtils.isNotEmpty(result)){
            setArtBond(result);
        }
        return result;
    }

    private void setArtBond(List<OperationsStoryDto> operationsStories) {
        if (CollectionUtils.isNotEmpty(operationsStories)) {
            Set<UUID> artBondIds = operationsStories.stream().map(OperationsStoryDto::getArtBondId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(artBondIds)) {
                List<ArtBondDto> artBonds = artBondService.getByIds(artBondIds);
                if (CollectionUtils.isNotEmpty(artBonds)) {
                    Map<UUID, ArtBondDto> artBondMap = artBonds.stream().collect(Collectors.toMap(ArtBondDto::getId, i -> i));
                    operationsStories.forEach(i -> i.setArtBond(artBondMap.get(i.getArtBondId())));
                }
            }
        }
    }

    private void setArtBond(OperationsStoryDto dto) {
        if (dto != null) {
            ArtBondDto artBond = artBondService.getById(dto.getArtBondId());
            if(artBond == null){
                throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
            }
            dto.setArtBond(artBond);
        }
    }

    private CustomerDto getCustomer(){
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.findByUserId(SecurityUtil.getUserId());
    }
}

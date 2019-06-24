package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.model.entity.business.BusinessCategoryEntity;
import com.gliesereum.karma.model.repository.jpa.business.BusinessCategoryRepository;
import com.gliesereum.karma.service.business.BusinessCategoryDescriptionService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessCategoryDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessCategoryDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class BusinessCategoryServiceImpl extends DefaultServiceImpl<BusinessCategoryDto, BusinessCategoryEntity> implements BusinessCategoryService {

    private static final Class<BusinessCategoryDto> DTO_CLASS = BusinessCategoryDto.class;
    private static final Class<BusinessCategoryEntity> ENTITY_CLASS = BusinessCategoryEntity.class;

    private final BusinessCategoryRepository businessCategoryRepository;

    @Autowired
    private BusinessCategoryDescriptionService businessCategoryDescriptionService;

    @Autowired
    public BusinessCategoryServiceImpl(BusinessCategoryRepository businessCategoryRepository, DefaultConverter defaultConverter) {
        super(businessCategoryRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.businessCategoryRepository = businessCategoryRepository;
    }

    @Override
    public List<BusinessCategoryDto> getAllSortByIndex() {
        List<BusinessCategoryEntity> entities = businessCategoryRepository.findAllByOrderByOrderIndexAsc();
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<BusinessCategoryDto> getByBusinessType(BusinessType businessType) {
        List<BusinessCategoryDto> result = null;
        if (businessType != null) {
            List<BusinessCategoryEntity> entities = businessCategoryRepository.findByBusinessType(businessType);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public BusinessCategoryDto getByCode(String code) {
        BusinessCategoryDto result = null;
        if (StringUtils.isNotBlank(code)) {
            BusinessCategoryEntity entity = businessCategoryRepository.findByCode(code);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public boolean existByCode(String code) {
        boolean result = false;
        if (StringUtils.isNotBlank(code)) {
            result = businessCategoryRepository.existsByCode(code);
        }
        return result;
    }

    @Override
    public boolean existsByCodeIdNot(String code, UUID id) {
        boolean result = false;
        if (StringUtils.isNotBlank(code)) {
            result = businessCategoryRepository.existsByCodeAndIdNot(code, id);
        }
        return result;
    }

    @Override
    public BusinessType checkAndGetType(UUID businessCategoryId) {
        if (businessCategoryId == null) {
            throw new ClientException(BUSINESS_CATEGORY_ID_EMPTY);
        }
        BusinessCategoryDto businessCategory = super.getById(businessCategoryId);
        if (businessCategory == null) {
            throw new ClientException(BUSINESS_CATEGORY_NOT_FOUND);
        }
        return businessCategory.getBusinessType();
    }

    @Override
    public BusinessCategoryDto create(BusinessCategoryDto dto) {
        BusinessCategoryDto result = null;
        if (dto != null) {
            throwExceptionIfCodeExist(dto.getCode());
            result = super.create(dto);
            DescriptionReadableDto<BusinessCategoryDescriptionDto> descriptions = businessCategoryDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    public BusinessCategoryDto update(BusinessCategoryDto dto) {
        BusinessCategoryDto result = null;
        if (dto != null) {
            throwExceptionIfCodeExist(dto.getCode(), dto.getId());
            result = super.update(dto);
            DescriptionReadableDto<BusinessCategoryDescriptionDto> descriptions = businessCategoryDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    private void throwExceptionIfCodeExist(String code, UUID id) {
        if (existsByCodeIdNot(code, id)) {
            throw new ClientException(BUSINESS_CATEGORY_BY_CODE_EXIST);
        }
    }

    private void throwExceptionIfCodeExist(String code) {
        if (existByCode(code)) {
            throw new ClientException(BUSINESS_CATEGORY_BY_CODE_EXIST);
        }
    }
}
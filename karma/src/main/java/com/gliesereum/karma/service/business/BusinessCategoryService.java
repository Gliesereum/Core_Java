package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.BusinessCategoryEntity;
import com.gliesereum.share.common.model.dto.karma.business.BusinessCategoryDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BusinessCategoryService extends DefaultService<BusinessCategoryDto, BusinessCategoryEntity> {

    List<BusinessCategoryDto> getByBusinessType(BusinessType businessType);

    BusinessCategoryDto getByCode(String code);

    boolean existByCode(String code);

    boolean existsByCodeIdNot(String code, UUID id);

    BusinessType checkAndGetType(UUID businessCategoryId);
}
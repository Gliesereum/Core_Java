package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.WorkingSpaceEntity;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceServicePriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkingSpaceService extends DefaultService<WorkingSpaceDto, WorkingSpaceEntity> {

    List<WorkingSpaceDto> getByBusinessId(UUID businessId, boolean setUsers);

    List<LiteWorkingSpaceDto> getLiteWorkingSpaceByBusinessId(UUID id);

    List<WorkingSpaceServicePriceDto> addServicePrice(List<WorkingSpaceServicePriceDto> dtos);
}
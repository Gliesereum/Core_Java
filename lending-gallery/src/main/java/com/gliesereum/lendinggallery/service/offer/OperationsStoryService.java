package com.gliesereum.lendinggallery.service.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface OperationsStoryService extends DefaultService<OperationsStoryDto, OperationsStoryEntity> {

    List<OperationsStoryDto> getAllByCustomerId(UUID customerId);

    List<OperationsStoryDto> getAllByUserId(UUID userId);

    List<OperationsStoryDto> getAllForUserByArtBondId(UUID artBondId);
}
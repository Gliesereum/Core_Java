package com.gliesereum.lendinggallery.service.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface OperationsStoryService extends DefaultService<OperationsStoryDto, OperationsStoryEntity> {

    List<OperationsStoryDto> getAllByCustomerId(UUID customerId);

    Map<UUID, List<OperationsStoryDto>> getAllByCustomerIds(List<UUID> customerIds);

    List<OperationsStoryDto> getAllByCustomerIdAndArtBondId(UUID customerId, UUID artBondId);

    List<OperationsStoryDto> getAllByUserId(UUID userId);

    List<OperationsStoryDto> getAllForUserByArtBondId(UUID artBondId);

    List<OperationsStoryDto> getAllByCustomerIdAndOperationType(UUID customerId, OperationType operationType);

    List<OperationsStoryDto> getAllPurchaseByArtBond(UUID artBondId);
}
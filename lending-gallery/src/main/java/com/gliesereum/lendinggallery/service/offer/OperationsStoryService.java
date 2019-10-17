package com.gliesereum.lendinggallery.service.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryFullDto;
import com.gliesereum.share.common.model.query.lendinggallery.offer.OperationsStoryQuery;
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

    Map<UUID, List<OperationsStoryDto>> getAllByArtBondIds(List<UUID> artBondIds);

    List<OperationsStoryDto> getAllByCustomerIdAndArtBondId(UUID customerId, UUID artBondId);

    List<OperationsStoryDto> getAllByUserId(UUID userId);

    List<UUID> getCustomerByArtBondId(UUID artBondId);

    List<UUID> getCustomerByArtBondId(List<UUID> artBondIds);

    List<OperationsStoryDto> filterByUserId(OperationsStoryQuery operationsStoryQuery, UUID userId);

    List<OperationsStoryDto> getAllForUserByArtBondId(UUID artBondId);

    List<OperationsStoryDto> getAllByCustomerIdAndOperationType(UUID customerId, OperationType operationType);

    List<OperationsStoryDto> getAllPurchaseByArtBond(UUID artBondId);

    List<OperationsStoryDto> getByArtBondIdAndOperationType(UUID artBondId, OperationType operationType);

    List<OperationsStoryFullDto> getByArtBondIdAndOperationTypeFull(UUID artBondId, OperationType operationType);
}
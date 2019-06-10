package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface OperationsStoryRepository extends JpaRepository<OperationsStoryEntity, UUID> {

    List<OperationsStoryEntity> findAllByCustomerIdOrderByCreate(UUID customerId);

    List<OperationsStoryEntity> findAllByCustomerIdAndArtBondIdOrderByCreate(UUID customerId, UUID artBondId);

    List<OperationsStoryEntity> findAllByCustomerIdAndOperationType(UUID customerId, OperationType operationType);

    List<OperationsStoryEntity> findAllByArtBondIdAndOperationType(UUID artBondId, OperationType operationType);

    List<OperationsStoryEntity> findAllByCustomerIdIn(List<UUID> customerIds);
}
package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface OperationsStoryRepository extends JpaRepository<OperationsStoryEntity, UUID> {

    List<OperationsStoryEntity> findAllByCustomerIdOrderByCreate(UUID customerId);

    List<OperationsStoryEntity> findAllByCustomerIdAndAtrBondIdOrderByCreate(UUID customerId, UUID artBondId);
}
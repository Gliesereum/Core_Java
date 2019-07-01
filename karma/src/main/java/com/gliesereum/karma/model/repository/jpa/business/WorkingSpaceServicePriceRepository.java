package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.WorkingSpaceServicePriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkingSpaceServicePriceRepository extends JpaRepository<WorkingSpaceServicePriceEntity, UUID> {
}
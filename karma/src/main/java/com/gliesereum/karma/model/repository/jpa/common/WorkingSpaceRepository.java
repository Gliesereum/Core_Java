package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.WorkingSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/17/18
 */
public interface WorkingSpaceRepository extends JpaRepository<WorkingSpaceEntity, UUID> {

    List<WorkingSpaceEntity> findByBusinessId(UUID businessId);
}

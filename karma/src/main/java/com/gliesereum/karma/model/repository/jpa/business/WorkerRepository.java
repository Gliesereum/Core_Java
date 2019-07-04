package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerRepository extends JpaRepository<WorkerEntity, UUID> {

    List<WorkerEntity> findAllByWorkingSpaceId(UUID workingSpaceId);

    List<WorkerEntity> findAllByBusinessId(UUID businessId);

    List<WorkerEntity> findAllByBusinessIdIn(List<UUID> businessIds);

    List<WorkerEntity> findAllByUserId(UUID userId);

    WorkerEntity findByUserIdAndBusinessId(UUID userId, UUID businessId);

    List<WorkerEntity> findByUserIdAndBusinessIdIn(UUID userId, List<UUID> businessId);
}

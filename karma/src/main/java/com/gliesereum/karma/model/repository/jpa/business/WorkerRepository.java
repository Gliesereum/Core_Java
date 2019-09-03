package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerRepository extends AuditableRepository<WorkerEntity> {

    List<WorkerEntity> findAllByWorkingSpaceId(UUID workingSpaceId);

    List<WorkerEntity> findAllByWorkingSpaceIdAndObjectState(UUID workingSpaceId, ObjectState objectState);

    List<WorkerEntity> findAllByBusinessId(UUID businessId);

    List<WorkerEntity> findAllByBusinessIdAndObjectState(UUID businessId, ObjectState objectState);

    List<WorkerEntity> findAllByBusinessIdIn(List<UUID> businessIds);

    List<WorkerEntity> findAllByBusinessIdInAndObjectState(List<UUID> businessIds, ObjectState objectState);

    List<WorkerEntity> findAllByUserId(UUID userId);

    List<WorkerEntity> findAllByUserIdAndObjectState(UUID userId, ObjectState objectState);

    WorkerEntity findByUserIdAndBusinessId(UUID userId, UUID businessId);

    WorkerEntity findByUserIdAndBusinessIdAndObjectState(UUID userId, UUID businessId, ObjectState objectState);

    List<WorkerEntity> findByUserIdAndBusinessIdIn(UUID userId, List<UUID> businessId);

    List<WorkerEntity> findByUserIdAndBusinessIdInAndObjectState(UUID userId, List<UUID> businessId, ObjectState objectState);

    boolean existsByUserIdAndCorporationId(UUID userId, UUID corporationId);

    boolean existsByUserIdAndCorporationIdAndObjectState(UUID userId, UUID corporationId, ObjectState objectState);

    boolean existsByUserIdAndBusinessId(UUID userId, UUID businessId);

    boolean existsByUserIdAndBusinessIdAndObjectState(UUID userId, UUID businessId, ObjectState objectState);

    Page<WorkerEntity> findAllByCorporationId(UUID corporationId, Pageable pageable);

    Page<WorkerEntity> findAllByCorporationIdAndObjectState(UUID corporationId, ObjectState objectState, Pageable pageable);

    Page<WorkerEntity> findAllByBusinessId(UUID businessId, Pageable pageable);

    Page<WorkerEntity> findAllByBusinessIdAndObjectState(UUID businessId, ObjectState objectState, Pageable pageable);
}

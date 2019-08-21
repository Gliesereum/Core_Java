package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface WorkerLockableRepository {

    List<WorkerEntity> findByWorkingSpaceIdWithLock(UUID workingSpaceId);
}

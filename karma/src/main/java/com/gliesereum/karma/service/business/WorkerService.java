package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.*;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerService extends DefaultService<WorkerDto, WorkerEntity> {

    List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId);

    WorkerDto findByUserIdAndBusinessId(UUID userId, UUID businessId);

    List<WorkerDto> findByUserId(UUID userId);

    boolean checkWorkerExistByPhone(String phone);

    List<WorkerDto> getByBusinessId(UUID businessId);

    List<LiteWorkerDto> getLiteWorkerByBusinessId(UUID id);

    List<LiteWorkerDto> getLiteWorkerByIds(List<UUID> ids);

    List<WorkerDto> findByUserIdAndCorporationId(UUID userId, UUID corporationId);

    List<WorkerDto> getByCorporationId(UUID corporationId);

    void setUsers(List<WorkerDto> result);

    boolean existByUserIdAndCorporationId(UUID userId, UUID corporationId);

    boolean existByUserIdAndBusinessId(UUID userId, UUID businessId);

    LiteWorkerDto getLiteWorkerById(UUID workerId);

    Map<UUID, LiteWorkerDto> getLiteWorkerMapByIds(Collection<UUID> collect);
}

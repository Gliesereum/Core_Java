package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerService extends DefaultService<WorkerDto, WorkerEntity> {

    List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId);

    WorkerDto findByUserIdAndBusinessId(UUID userId, UUID businessId);

    List<WorkerDto> findByUserId(UUID userId);

    boolean checkWorkerExistByPhone(String phone);
}

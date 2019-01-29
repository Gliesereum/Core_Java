package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.WorkerEntity;
import com.gliesereum.share.common.model.dto.karma.common.WorkerDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerService extends DefaultService<WorkerDto, WorkerEntity> {

    List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId);
}

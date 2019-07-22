package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.WorkTimeEntity;
import com.gliesereum.share.common.model.dto.karma.business.WorkTimeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkTimeService extends DefaultService<WorkTimeDto, WorkTimeEntity> {

    List<WorkTimeDto> getByObjectId(UUID businessId);

    void delete(UUID id, UUID businessCategoryId);

    void deleteByObjectId(UUID id);
}

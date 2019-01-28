package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.FilterAttributeEntity;
import com.gliesereum.share.common.model.dto.karma.common.FilterAttributeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FilterAttributeService extends DefaultService<FilterAttributeDto, FilterAttributeEntity> {

    List<FilterAttributeDto> getByFilterId(UUID filterId);

    void checkFilterAttributeExist(UUID id);
}

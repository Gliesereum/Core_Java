package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceService extends DefaultService<ServiceDto, ServiceEntity> {

    List<ServiceDto> getAllByBusinessCategoryId(UUID businessCategoryId);
}

package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.PackageEntity;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface PackageService extends DefaultService<PackageDto, PackageEntity> {

    List<PackageDto> getByBusinessServiceId(UUID id);
}

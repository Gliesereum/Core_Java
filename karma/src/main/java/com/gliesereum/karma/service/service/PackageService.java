package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.PackageEntity;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PackageService extends DefaultService<PackageDto, PackageEntity> {

    List<PackageDto> getByBusinessId(UUID id);

    PackageDto getByIdIgnoreState(UUID id);

    List<LitePackageDto> getLitePackageByBusinessId(UUID id);
}

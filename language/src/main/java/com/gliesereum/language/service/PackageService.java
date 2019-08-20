package com.gliesereum.language.service;

import com.gliesereum.language.model.entity.PackageEntity;
import com.gliesereum.share.common.model.dto.language.PackageDto;
import com.gliesereum.share.common.model.dto.language.PackageMapDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface PackageService extends DefaultService<PackageDto, PackageEntity> {

    List<PackageDto> getByModule(String module);

    PackageDto create(PackageDto packageDto, UUID createFrom);

    PackageMapDto getMapByModule(String module);
}

package com.gliesereum.karma.service.location;

import com.gliesereum.karma.model.entity.location.DistrictEntity;
import com.gliesereum.share.common.model.dto.karma.location.DistrictDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

public interface DistrictService extends DefaultService<DistrictDto, DistrictEntity> {

    DistrictDto addGeoPosition(List<GeoPositionDto> positions, UUID id);

    List<DistrictDto> getAllByCityId(UUID cityId);
}
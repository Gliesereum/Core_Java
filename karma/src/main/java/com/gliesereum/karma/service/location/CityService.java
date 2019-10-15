package com.gliesereum.karma.service.location;

import com.gliesereum.karma.model.entity.location.CityEntity;
import com.gliesereum.share.common.model.dto.karma.location.CityDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

public interface CityService extends DefaultService<CityDto, CityEntity> {

    CityDto addGeoPosition(List<GeoPositionDto> positions, UUID id);
}
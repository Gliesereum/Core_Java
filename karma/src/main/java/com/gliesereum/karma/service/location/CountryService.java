package com.gliesereum.karma.service.location;

import com.gliesereum.karma.model.entity.location.CountryEntity;
import com.gliesereum.share.common.model.dto.karma.location.CountryDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

public interface CountryService extends DefaultService<CountryDto, CountryEntity> {

    CountryDto addGeoPosition(List<GeoPositionDto> positions, UUID id);
}
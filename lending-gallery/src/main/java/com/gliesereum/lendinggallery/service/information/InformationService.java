package com.gliesereum.lendinggallery.service.information;

import com.gliesereum.lendinggallery.model.entity.information.InformationEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.information.InformationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

public interface InformationService extends DefaultService<InformationDto, InformationEntity> {

    List<InformationDto> getByTag(String tag);

    List<InformationDto> getByTagAndIsoCode(String tag, String isoCode);
}
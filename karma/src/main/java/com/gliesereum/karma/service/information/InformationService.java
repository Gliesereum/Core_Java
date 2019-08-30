package com.gliesereum.karma.service.information;

import com.gliesereum.karma.model.entity.information.InformationEntity;
import com.gliesereum.share.common.model.dto.karma.information.InformationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface InformationService extends DefaultService<InformationDto, InformationEntity> {

    List<InformationDto> getByTag(String tag);

    List<InformationDto> getByTagAndIsoCode(String tag, String isoCode);
}
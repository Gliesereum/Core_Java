package com.gliesereum.lendinggallery.service.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondService extends DefaultService<ArtBondDto, ArtBondEntity> {

    List<ArtBondDto> getAllByStatus(StatusType status);

    ArtBondDto getArtBondById(UUID id);
}

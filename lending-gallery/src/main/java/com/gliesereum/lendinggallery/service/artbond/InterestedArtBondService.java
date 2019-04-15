package com.gliesereum.lendinggallery.service.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.InterestedArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.InterestedArtBondDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface InterestedArtBondService extends DefaultService<InterestedArtBondDto, InterestedArtBondEntity> {

    InterestedArtBondDto getByArtBondIdAndCustomerId(UUID artBondId, UUID customerId);

    List<InterestedArtBondDto> getByArtBondId(UUID artBondId);
}    
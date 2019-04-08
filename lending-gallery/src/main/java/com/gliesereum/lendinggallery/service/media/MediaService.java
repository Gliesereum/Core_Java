package com.gliesereum.lendinggallery.service.media;

import com.gliesereum.lendinggallery.model.entity.media.MediaEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import com.gliesereum.share.common.model.dto.lendinggallery.media.MediaDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MediaService extends DefaultService<MediaDto, MediaEntity> {

    void delete(UUID id, UUID objectId);

    List<MediaDto> getByObjectIdAndType(UUID objectId, BlockMediaType blockMediaType);

    ArtBondDto createList(List<MediaDto> files, UUID id);

    void deleteAllByObjectId(UUID objectId);
}

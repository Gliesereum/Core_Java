package com.gliesereum.karma.service.media;

import com.gliesereum.karma.model.entity.media.MediaEntity;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface MediaService extends DefaultService<MediaDto, MediaEntity> {

    List<MediaDto> getByObjectId(UUID objectId);

    Map<UUID, List<MediaDto>> getMapByObjectIds(List<UUID> objectIds);

   void delete(UUID id, UUID objectId);
}

package com.gliesereum.karma.service.media;

import com.gliesereum.karma.model.entity.media.MediaEntity;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */
public interface MediaService extends DefaultService<MediaDto, MediaEntity> {

    List<MediaDto> getByObjectId(UUID objectId);
}

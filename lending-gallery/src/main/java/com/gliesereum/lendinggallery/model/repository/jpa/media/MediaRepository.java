package com.gliesereum.lendinggallery.model.repository.jpa.media;

import com.gliesereum.lendinggallery.model.entity.media.MediaEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MediaRepository extends JpaRepository<MediaEntity, UUID> {

    List<MediaEntity> findByObjectIdAndBlockMediaType(UUID objectId, BlockMediaType blockMediaType);

    MediaEntity findByIdAndObjectId(UUID id, UUID objectId);
}

package com.gliesereum.karma.model.repository.jpa.media;

import com.gliesereum.karma.model.entity.media.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */
@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, UUID> {

    List<MediaEntity> findByObjectId(UUID objectId);

    MediaEntity findByIdAndObjectId(UUID id, UUID objectId);
}

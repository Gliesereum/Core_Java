package com.gliesereum.lendinggallery.model.repository.jpa.content;

import com.gliesereum.lendinggallery.model.entity.content.ContentEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ContentRepository extends JpaRepository<ContentEntity, UUID> {

    List<ContentEntity> findAllByContentType(ContentType type);
}
package com.gliesereum.lendinggallery.model.repository.jpa.content;

import com.gliesereum.lendinggallery.model.entity.content.ContentEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ContentRepository extends JpaRepository<ContentEntity, UUID> {

    List<ContentEntity> findAllByContentTypeOrderByCreate(ContentType type, Pageable pageable);

    /**
     * Content carrying any of the given tags. `tags` is an @ElementCollection,
     * so this joins the content_tag table; Distinct keeps a row from being
     * returned once per matching tag.
     */
    List<ContentEntity> findDistinctByTagsInOrderByCreate(Collection<String> tags, Pageable pageable);
}
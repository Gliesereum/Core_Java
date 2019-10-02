package com.gliesereum.karma.model.repository.jpa.tag;

import com.gliesereum.karma.model.entity.tag.TagEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;

import java.util.List;
import java.util.UUID;


public interface TagRepository extends AuditableRepository <TagEntity> {

    List<TagEntity> getAllByIdInAndObjectStateIn(List<UUID> ids, List<ObjectState> states);
    
    List<TagEntity> getAllByNameInAndObjectStateIn(List<String> names, List<ObjectState> states);
}
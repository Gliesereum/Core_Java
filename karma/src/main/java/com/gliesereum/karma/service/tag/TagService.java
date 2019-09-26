package com.gliesereum.karma.service.tag;

import com.gliesereum.karma.model.entity.tag.TagEntity;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.auditable.AuditableService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TagService extends AuditableService<TagDto, TagEntity> {

    Map<UUID, TagDto> getMapByIds(List<UUID> ids, List<ObjectState> states);
}
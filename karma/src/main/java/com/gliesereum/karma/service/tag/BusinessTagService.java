package com.gliesereum.karma.service.tag;

import com.gliesereum.karma.model.entity.tag.BusinessTagEntity;
import com.gliesereum.share.common.model.dto.karma.tag.BusinessTagDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BusinessTagService extends DefaultService<BusinessTagDto, BusinessTagEntity> {
   
    List<TagDto> addTag(UUID tagId, UUID businessId);

    List<TagDto> removeTag(UUID tagId, UUID businessId);

    List<UUID> getTagIdsByBusinessId(UUID businessId);

    void deleteByBusinessId(UUID businessId);

    Map<UUID, List<TagDto>> getMapByBusinessIds(List<UUID> ids);
}
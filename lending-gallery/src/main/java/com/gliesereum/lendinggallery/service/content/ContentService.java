package com.gliesereum.lendinggallery.service.content;

import com.gliesereum.lendinggallery.model.entity.content.ContentEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.content.ContentDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ContentService extends DefaultService<ContentDto, ContentEntity> {

    List<ContentDto> getAllByContentType(ContentType type, Integer page, Integer size);

    List<ContentDto> getAllByTags(List<String> tags, Integer page, Integer size);
}
package com.gliesereum.karma.service.media.impl;

import com.gliesereum.karma.model.entity.media.MediaEntity;
import com.gliesereum.karma.model.repository.jpa.media.MediaRepository;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Service
public class MediaServiceImpl extends DefaultServiceImpl<MediaDto, MediaEntity> implements MediaService {

    private static final Class<MediaDto> DTO_CLASS = MediaDto.class;
    private static final Class<MediaEntity> ENTITY_CLASS = MediaEntity.class;

    private MediaRepository mediaRepository;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, DefaultConverter defaultConverter) {
        super(mediaRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.mediaRepository = mediaRepository;
    }

    @Override
    public List<MediaDto> getByObjectId(UUID objectId) {
        List<MediaDto> result = null;
        if (objectId != null) {
            List<MediaEntity> entities = mediaRepository.findByObjectId(objectId);
            result = converter.convert(entities, dtoClass);
        }

        return result;
    }
}

package com.gliesereum.karma.service.media.impl;

import com.gliesereum.karma.model.entity.media.MediaEntity;
import com.gliesereum.karma.model.repository.jpa.media.MediaRepository;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.dto.karma.media.MediaListUpdateDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.MEDIA_NOT_FOUND_BY_ID;

/**
 * @author yvlasiuk
 * @version 1.0
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
    @Transactional
    public List<MediaDto> updateList(MediaListUpdateDto medias) {
        List<MediaDto> result = Collections.emptyList();
        UUID objectId = medias.getObjectId();
        if (objectId != null) {
            mediaRepository.deleteAllByObjectId(objectId);
            List<MediaDto> list = medias.getList();
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(i -> {
                    i.setObjectId(objectId);
                    i.setId(null);
                });
                result = super.create(list);
            }
        }
        return result;
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

    @Override
    public Map<UUID, List<MediaDto>> getMapByObjectIds(List<UUID> objectIds) {
        Map<UUID, List<MediaDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(objectIds)) {
            List<MediaEntity> entities = mediaRepository.findAllByObjectIdIn(objectIds);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = entities.stream().map(i -> converter.convert(i, dtoClass)).collect(Collectors.groupingBy(MediaDto::getObjectId));
            }
        }
        return result;
    }

    @Override
    public void delete(UUID id, UUID objectId) {
        MediaEntity media = mediaRepository.findByIdAndObjectId(id, objectId);
        if (media == null) {
            throw new ClientException(MEDIA_NOT_FOUND_BY_ID);
        }
        mediaRepository.delete(media);
    }
}

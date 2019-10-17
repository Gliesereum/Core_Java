package com.gliesereum.lendinggallery.service.media.impl;

import com.gliesereum.lendinggallery.model.entity.media.MediaEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.media.MediaRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.media.MediaService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import com.gliesereum.share.common.model.dto.lendinggallery.media.MediaDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.*;


/**
 * @author vitalij
 * @version 1.0
 */

@Service
public class MediaServiceImpl extends DefaultServiceImpl<MediaDto, MediaEntity> implements MediaService {

    private static final Class<MediaDto> DTO_CLASS = MediaDto.class;
    private static final Class<MediaEntity> ENTITY_CLASS = MediaEntity.class;

    private final MediaRepository mediaRepository;

    @Autowired
    private ArtBondService artBondService;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, DefaultConverter defaultConverter) {
        super(mediaRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.mediaRepository = mediaRepository;
    }

    @Override
    public void delete(UUID id, UUID objectId) {
        MediaEntity media = mediaRepository.findByIdAndObjectId(id, objectId);
        if (media == null) {
            throw new ClientException(MEDIA_NOT_FOUND_BY_ID);
        }
        repository.delete(media);
    }

    @Override
    public List<MediaDto> getByObjectIdAndType(UUID id, BlockMediaType blockMediaType) {
        List<MediaDto> result = null;
        List<MediaEntity> entities = mediaRepository.findByObjectIdAndBlockMediaType(id, blockMediaType);
        result = converter.convert(entities, dtoClass);
        if(result == null){
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public ArtBondDto createList(List<MediaDto> files, UUID id) {
        ArtBondDto result = artBondService.getById(id);
        if(result == null){
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        if(CollectionUtils.isNotEmpty(files)){
         files.forEach(f->{
             if (f == null) {
                 throw new ClientException(MODEL_IS_EMPTY);
             }
             if(f.getBlockMediaType() == null){
                 throw new ClientException(BLOCK_MEDIA_TYPE_IS_EMPTY);
             }
             f.setObjectId(id);
         });
            List<MediaDto> toUpdate = files.stream().filter(i -> i.getId() != null).collect(Collectors.toList());
            List<MediaDto> toCreate = files.stream().filter(i -> i.getId() == null).collect(Collectors.toList());
            update(toUpdate);
            create(toCreate); //todo check
        }
        return result;
    }

    @Override
    public MediaDto create(MediaDto dto) {
        checkMedia(dto);
        return super.create(dto);
    }

    @Override
    public MediaDto update(MediaDto dto) {
        checkMedia(dto);
        return super.update(dto);
    }

    @Override
    @Transactional
    public void deleteAllByObjectId(UUID objectId) {
        if (objectId != null) {
            mediaRepository.deleteAllByObjectId(objectId);
        }
    }

    private void checkMedia(MediaDto dto) {
        if (dto == null) {
            throw new ClientException(MODEL_IS_EMPTY);
        }
        if(dto.getBlockMediaType() == null){
            throw new ClientException(BLOCK_MEDIA_TYPE_IS_EMPTY);
        }
        ArtBondDto artBond = artBondService.getById(dto.getObjectId());
        if(artBond == null){
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
    }
}

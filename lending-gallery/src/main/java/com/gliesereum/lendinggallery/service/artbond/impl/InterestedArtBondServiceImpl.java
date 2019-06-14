package com.gliesereum.lendinggallery.service.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.InterestedArtBondEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.InterestedArtBondRepository;
import com.gliesereum.lendinggallery.service.artbond.InterestedArtBondService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.InterestedArtBondDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ID_IS_EMPTY;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class InterestedArtBondServiceImpl extends DefaultServiceImpl<InterestedArtBondDto, InterestedArtBondEntity> implements InterestedArtBondService {

    private static final Class<InterestedArtBondDto> DTO_CLASS = InterestedArtBondDto.class;
    private static final Class<InterestedArtBondEntity> ENTITY_CLASS = InterestedArtBondEntity.class;

    private final InterestedArtBondRepository interestedArtBondRepository;

    public InterestedArtBondServiceImpl(InterestedArtBondRepository interestedArtBondRepository, DefaultConverter defaultConverter) {
        super(interestedArtBondRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.interestedArtBondRepository = interestedArtBondRepository;
    }

    @Override
    public InterestedArtBondDto getByArtBondIdAndCustomerId(UUID artBondId, UUID customerId) {
        InterestedArtBondEntity entity = interestedArtBondRepository.findByArtBondIdAndCustomerId(artBondId, customerId);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<InterestedArtBondDto> getByArtBondId(UUID artBondId) {
        if (artBondId == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        List<InterestedArtBondEntity> entity = interestedArtBondRepository.findByArtBondId(artBondId);
        return converter.convert(entity, dtoClass);
    }

}

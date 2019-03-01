package com.gliesereum.lendinggallery.service.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.ArtBondRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.media.MediaService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ART_BOND_NOT_FOUND_BY_ID;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ID_IS_EMPTY;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ArtBondServiceImpl extends DefaultServiceImpl<ArtBondDto, ArtBondEntity> implements ArtBondService {

    @Autowired
    private ArtBondRepository repository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private InvestorOfferService investorOfferService;

    @Autowired
    private OperationsStoryService storyService;

    private static final Class<ArtBondDto> DTO_CLASS = ArtBondDto.class;
    private static final Class<ArtBondEntity> ENTITY_CLASS = ArtBondEntity.class;

    public ArtBondServiceImpl(ArtBondRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<ArtBondDto> getAllByStatus(StatusType status) {
        List<ArtBondEntity> entities = repository.findAllByStatusTypeAndSpecialStatusType(status, SpecialStatusType.ACTIVE);
        List<ArtBondDto> result = converter.convert(entities, dtoClass);
        result.forEach(f -> setMedia(f));
        return result;
    }

    @Override
    public List<ArtBondDto> getAll() {
        List<ArtBondDto> result = super.getAll();
        result.forEach(f -> setMedia(f));
        return result;
    }

    @Override
    public ArtBondDto create(ArtBondDto dto) {
        dto.setStatusType(StatusType.WAITING_COLLECTION);
        dto.setSpecialStatusType(SpecialStatusType.ACTIVE);
        return super.create(dto);
    }

    @Override
    public ArtBondDto update(ArtBondDto dto) {
        ArtBondDto artBond = getById(dto.getId());
        dto.setStatusType(artBond.getStatusType());
        dto.setSpecialStatusType(artBond.getSpecialStatusType());
        ArtBondDto result = super.update(dto);
        setMedia(result);
        return result;
    }

    @Override
    public ArtBondDto getById(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        return super.getById(id);
    }

    @Override
    public ArtBondDto getArtBondById(UUID id) {
        ArtBondDto result = getById(id);
        if (result == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        setMedia(result);
        return result;
    }

    @Override
    @Transactional
    public ArtBondDto updateStatus(StatusType status, UUID id) {
        ArtBondDto artBond = getById(id);
        if (artBond == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        if (artBond.getStatusType().equals(status)) {
            return artBond;
        }
        artBond.setStatusType(status);
        if (status.equals(StatusType.COMPLETED_COLLECTION)) {
            List<InvestorOfferDto> offers = investorOfferService.getAllByArtBond(id);
            if (CollectionUtils.isNotEmpty(offers)) {
                offers.forEach(f -> {
                    OperationsStoryDto story = new OperationsStoryDto();
                    story.setName(artBond.getName());
                    story.setSum(f.getSumInvestment());
                    story.setDescription(null);
                    story.setArtBondId(f.getArtBondId());
                    story.setCreate(LocalDateTime.now());
                    story.setOperationType(OperationType.PURCHASE);
                    story.setCustomerId(f.getCustomerId());
                    storyService.create(story);
                });
            }
        }
        return super.update(artBond);
    }

    private void setMedia(ArtBondDto dto) {
        dto.setImages(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.IMAGES));
        dto.setArtBondInfo(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.ART_BOND_INFO));
        dto.setAuthorInfo(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.AUTHOR_INFO));
        dto.setDocuments(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.DOCUMENTS));
    }
}

package com.gliesereum.lendinggallery.service.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondTokenEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.ArtBondTokenRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondTokenService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondTokenDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ArtBondTokenServiceImpl extends DefaultServiceImpl<ArtBondTokenDto, ArtBondTokenEntity> implements ArtBondTokenService {

    private static final Class<ArtBondTokenDto> DTO_CLASS = ArtBondTokenDto.class;
    private static final Class<ArtBondTokenEntity> ENTITY_CLASS = ArtBondTokenEntity.class;

    private final ArtBondTokenRepository artBondTokenRepository;

    @Autowired
    public ArtBondTokenServiceImpl(ArtBondTokenRepository artBondTokenRepository, DefaultConverter defaultConverter) {
        super(artBondTokenRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.artBondTokenRepository = artBondTokenRepository;
    }
}
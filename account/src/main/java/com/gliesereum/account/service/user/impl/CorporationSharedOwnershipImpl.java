package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationSharedOwnershipEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationSharedOwnershipRepository;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class CorporationSharedOwnershipImpl extends DefaultServiceImpl<CorporationSharedOwnershipDto, CorporationSharedOwnershipEntity> implements CorporationSharedOwnershipService {

    @Autowired
    CorporationSharedOwnershipRepository repository;

    private static final Class<CorporationSharedOwnershipDto> DTO_CLASS = CorporationSharedOwnershipDto.class;
    private static final Class<CorporationSharedOwnershipEntity> ENTITY_CLASS = CorporationSharedOwnershipEntity.class;

    public CorporationSharedOwnershipImpl(CorporationSharedOwnershipRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<CorporationSharedOwnershipDto> getAllByUserId(UUID id) {
        List<CorporationSharedOwnershipEntity> entities = repository.findByOwnerId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<CorporationSharedOwnershipDto> getAllByCorporationOwnerId(UUID id) {
        List<CorporationSharedOwnershipEntity> entities = repository.findByCorporationOwnerId(id);
        return converter.convert(entities, dtoClass);
    }
}

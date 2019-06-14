package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationSharedOwnershipEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationSharedOwnershipRepository;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class CorporationSharedOwnershipImpl extends DefaultServiceImpl<CorporationSharedOwnershipDto, CorporationSharedOwnershipEntity> implements CorporationSharedOwnershipService {

    private static final Class<CorporationSharedOwnershipDto> DTO_CLASS = CorporationSharedOwnershipDto.class;
    private static final Class<CorporationSharedOwnershipEntity> ENTITY_CLASS = CorporationSharedOwnershipEntity.class;

    private final CorporationSharedOwnershipRepository corporationSharedOwnershipRepository;

    public CorporationSharedOwnershipImpl(CorporationSharedOwnershipRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.corporationSharedOwnershipRepository = repository;
    }

    @Override
    public List<CorporationSharedOwnershipDto> getAllByUserId(UUID id) {
        List<CorporationSharedOwnershipEntity> entities = corporationSharedOwnershipRepository.findByOwnerId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<CorporationSharedOwnershipDto> getAllByCorporationOwnerId(UUID id) {
        List<CorporationSharedOwnershipEntity> entities = corporationSharedOwnershipRepository.findByCorporationOwnerId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<UUID> getAllCorporationIdByUserId(UUID id) {
        List<UUID> result = null;
        List<CorporationSharedOwnershipDto> founded = getAllByUserId(id);
        if (CollectionUtils.isNotEmpty(founded)) {
            result = founded.stream()
                    .map(CorporationSharedOwnershipDto::getCorporationId)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<CorporationSharedOwnershipDto> getAllByUserIds(List<UUID> userIds) {
        List<CorporationSharedOwnershipDto> result = null;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<CorporationSharedOwnershipEntity> entities = corporationSharedOwnershipRepository.findAllByOwnerIdIn(userIds);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}

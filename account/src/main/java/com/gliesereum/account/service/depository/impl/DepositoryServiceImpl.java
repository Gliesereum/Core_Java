package com.gliesereum.account.service.depository.impl;

import com.gliesereum.account.model.entity.DepositoryEntity;
import com.gliesereum.account.model.repository.jpa.user.DepositoryRepository;
import com.gliesereum.account.service.depository.DepositoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.DepositoryDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
@Slf4j
@Service
public class DepositoryServiceImpl extends DefaultServiceImpl<DepositoryDto, DepositoryEntity> implements DepositoryService {

    private static final Class<DepositoryDto> DTO_CLASS = DepositoryDto.class;
    private static final Class<DepositoryEntity> ENTITY_CLASS = DepositoryEntity.class;

    DepositoryRepository repository;

    public DepositoryServiceImpl(DepositoryRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<DepositoryDto> getByOwnerId(UUID ownerId) {
        List<DepositoryEntity> entities = repository.findByOwnerId(ownerId);
        return converter.convert(entities, dtoClass);
    }
}

package com.gliesereum.permission.service.application.impl;

import com.gliesereum.permission.model.entity.application.ApplicationEntity;
import com.gliesereum.permission.model.repository.jpa.application.ApplicationRepository;
import com.gliesereum.permission.service.application.ApplicationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_NOT_ACTIVE;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class ApplicationServiceImpl extends DefaultServiceImpl<ApplicationDto, ApplicationEntity> implements ApplicationService {

    private static final Class<ApplicationDto> DTO_CLASS = ApplicationDto.class;
    private static final Class<ApplicationEntity> ENTITY_CLASS = ApplicationEntity.class;

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, DefaultConverter defaultConverter) {
        super(applicationRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.applicationRepository = applicationRepository;
    }

    @Override
    public ApplicationDto check(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        Optional<ApplicationEntity> byId = applicationRepository.findById(id);
        ApplicationEntity entity = byId.orElseThrow(() -> new ClientException(APPLICATION_NOT_FOUND));
        if (!entity.getIsActive()) {
            throw new ClientException(APPLICATION_NOT_ACTIVE);
        }
        return converter.convert(entity, dtoClass);
    }
}
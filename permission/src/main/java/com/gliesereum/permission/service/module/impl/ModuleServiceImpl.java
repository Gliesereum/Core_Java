package com.gliesereum.permission.service.module.impl;

import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.permission.service.module.ModuleService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@Service
public class ModuleServiceImpl extends DefaultServiceImpl<ModuleDto, ModuleEntity> implements ModuleService {

    private static final Class<ModuleDto> DTO_CLASS = ModuleDto.class;
    private static final Class<ModuleEntity> ENTITY_CLASS = ModuleEntity.class;

    public ModuleServiceImpl(JpaRepository<ModuleEntity, UUID> repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }
}

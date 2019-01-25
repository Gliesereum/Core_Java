package com.gliesereum.permission.service.module.impl;

import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.permission.model.repository.jpa.module.ModuleRepository;
import com.gliesereum.permission.service.module.ModuleService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ModuleServiceImpl extends DefaultServiceImpl<ModuleDto, ModuleEntity> implements ModuleService {

    public static final String DEFAULT_VERSION = "v1";

    private static final Class<ModuleDto> DTO_CLASS = ModuleDto.class;
    private static final Class<ModuleEntity> ENTITY_CLASS = ModuleEntity.class;

    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository, DefaultConverter defaultConverter) {
        super(moduleRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleDto getByUrl(String url) {
        ModuleDto result = null;
        if (StringUtils.isNotEmpty(url)) {
            ModuleEntity entity = moduleRepository.findByUrl(url);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public ModuleDto getByUrlOrCreate(String url, String name) {
        ModuleDto result = null;
        if (StringUtils.isNotEmpty(url)) {
            result = getByUrl(url);
            if (result == null) {
                ModuleEntity entity = new ModuleEntity();
                entity.setUrl(url);
                entity.setTitle(name);
                entity.setVersion(DEFAULT_VERSION);
                entity.setIsActive(true);
                entity = moduleRepository.save(entity);
                result = converter.convert(entity, dtoClass);
            }
        }
        return result;
    }
}

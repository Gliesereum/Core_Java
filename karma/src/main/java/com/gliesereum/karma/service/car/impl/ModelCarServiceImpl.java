package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.ModelCarEntity;
import com.gliesereum.karma.model.repository.jpa.car.ModelCarRepository;
import com.gliesereum.karma.service.car.ModelCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.ModelCarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class ModelCarServiceImpl extends DefaultServiceImpl<ModelCarDto, ModelCarEntity> implements ModelCarService {

    @Autowired
    private ModelCarRepository repository;

    private static final Class<ModelCarDto> DTO_CLASS = ModelCarDto.class;
    private static final Class<ModelCarEntity> ENTITY_CLASS = ModelCarEntity.class;

    public ModelCarServiceImpl(ModelCarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<ModelCarDto> getAllByBrandId(UUID id) {
        List<ModelCarEntity> entities = repository.getAllByBrandId(id);
        return converter.convert(entities, dtoClass);
    }
}

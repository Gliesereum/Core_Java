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

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ModelCarServiceImpl extends DefaultServiceImpl<ModelCarDto, ModelCarEntity> implements ModelCarService {

    private static final Class<ModelCarDto> DTO_CLASS = ModelCarDto.class;
    private static final Class<ModelCarEntity> ENTITY_CLASS = ModelCarEntity.class;

    private ModelCarRepository modelCarRepository;

    @Autowired
    public ModelCarServiceImpl(ModelCarRepository modelCarRepository, DefaultConverter defaultConverter) {
        super(modelCarRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.modelCarRepository = modelCarRepository;
    }

    @Override
    public List<ModelCarDto> getAllByBrandId(UUID id) {
        List<ModelCarEntity> entities = modelCarRepository.getAllByBrandId(id);
        List<ModelCarDto> result = converter.convert(entities, dtoClass);
        result.sort(Comparator.comparing(ModelCarDto::getName));
        return result;
    }

    @Override
    public List<ModelCarDto> getAll() {
        List<ModelCarDto> result = super.getAll();
        result.sort(Comparator.comparing(ModelCarDto::getName));
        return result;
    }
}

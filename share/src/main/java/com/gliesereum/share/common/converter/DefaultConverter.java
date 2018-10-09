package com.gliesereum.share.common.converter;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public class DefaultConverter {

    private ModelMapper modelMapper;

    public DefaultConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public  <E extends DefaultEntity, D extends DefaultDto> E dtoToEntity(D dto, Class<E> entityClass) {
        E entity = null;
        if (dto != null) {
            entity = modelMapper.map(dto, entityClass);
        }
        return entity;
    }

    public <E extends DefaultEntity, D extends DefaultDto> List<E> dtoToEntity(List<D> dtos, Class<E> entityClass) {
        List<E> entities = null;
        if (CollectionUtils.isNotEmpty(dtos)) {
            entities = new ArrayList<>(dtos.size());
            for (D dto : dtos) {
                entities.add(dtoToEntity(dto, entityClass));
            }
        }
        return entities;
    }

    public <E extends DefaultEntity, D extends DefaultDto> D entityToDto(E entity, Class<D> dtoClass) {
        D dto = null;
        if (entity != null) {
            dto = modelMapper.map(entity, dtoClass);
        }
        return dto;
    }

    public <E extends DefaultEntity, D extends DefaultDto> List<D> entityToDto(List<E> entities, Class<D> dtoClass) {
        List<D> dtos = null;
        if (CollectionUtils.isNotEmpty(entities)) {
            dtos = new ArrayList<>(entities.size());
            for (E entity : entities) {
                dtos.add(entityToDto(entity, dtoClass));
            }
        }
        return dtos;
    }
}

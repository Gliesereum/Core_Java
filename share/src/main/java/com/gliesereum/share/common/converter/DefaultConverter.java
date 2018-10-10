package com.gliesereum.share.common.converter;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 10/10/2018
 */
public interface DefaultConverter {

    <E extends DefaultEntity, D extends DefaultDto> E dtoToEntity(D dto, Class<E> entityClass);

    <E extends DefaultEntity, D extends DefaultDto> List<E> dtoToEntity(List<D> dtos, Class<E> entityClass);

    <E extends DefaultEntity, D extends DefaultDto> D entityToDto(E entity, Class<D> dtoClass);

    <E extends DefaultEntity, D extends DefaultDto> List<D> entityToDto(List<E> entities, Class<D> dtoClass);
}

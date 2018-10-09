package com.gliesereum.share.common.service;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public abstract class DefaultServiceImpl<D extends DefaultDto, E extends DefaultEntity> implements DefaultService<D, E> {

    protected JpaRepository<E, UUID> repository;

    protected DefaultConverter converter;

    protected Class<D> dtoClass;

    protected Class<E> entityClass;

    public DefaultServiceImpl(JpaRepository<E, UUID> repository, DefaultConverter defaultConverter, Class<D> dtoClass, Class<E> entityClass) {
        this.repository = repository;
        this.converter = defaultConverter;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    public D create(D dto) {
        if (dto != null) {
            E entity = converter.dtoToEntity(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.entityToDto(entity, dtoClass);
        }
        return dto;
    }

    public D update(D dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                //TODO: set custom exception
                throw new RuntimeException("Id not specified");
            }
            E entity = converter.dtoToEntity(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.entityToDto(entity, dtoClass);
        }
        return dto;
    }

    public D getById(UUID id) {
        D result = null;
        if (id != null) {
            Optional<E> entityOptional = repository.findById(id);
            if (entityOptional.isPresent()) {
                result = converter.entityToDto(entityOptional.get(), dtoClass);
            }
        }
        return result;
    }

    public List<D> getAll() {
        List<E> entities = repository.findAll();
        return converter.entityToDto(entities, dtoClass);
    }

    public void delete(UUID id) {
        if (id != null) {
            Optional<E> entity = repository.findById(id);
            entity.ifPresent(i -> repository.delete(i));

        }
    }
}

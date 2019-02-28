package com.gliesereum.share.common.service;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;

/**
 * @author yvlasiuk
 * @version 1.0
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
            dto.setId(null);
            E entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    public List<D> create(List<D> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            List<E> entities = converter.convert(dtos, entityClass);
            entities = repository.saveAll(entities);
            repository.flush();
            dtos = converter.convert(entities, dtoClass);
        }
        return dtos;
    }

    public D update(D dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            E entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    public D getById(UUID id) {
        D result = null;
        if (id != null) {
            Optional<E> entityOptional = repository.findById(id);
            if (entityOptional.isPresent()) {
                result = converter.convert(entityOptional.get(), dtoClass);
            }
        }
        return result;
    }

    @Override
    public List<D> getByIds(Iterable<UUID> ids) {
        List<D> result = null;
        if (ids != null) {
            List<E> entities = repository.findAllById(ids);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    public List<D> getAll() {
        List<E> entities = repository.findAll();
        return converter.convert(entities, dtoClass);
    }

    @Transactional
    public void delete(UUID id) {
        if (id != null) {
            Optional<E> entity = repository.findById(id);
            entity.ifPresent(i -> repository.delete(i));

        }
    }

    @Override
    public boolean isExist(UUID id) {
        return repository.existsById(id);
    }
}

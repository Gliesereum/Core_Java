package com.gliesereum.share.common.service;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.NOT_EXIST_BY_ID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public abstract class DefaultServiceImpl<D extends DefaultDto, E extends DefaultEntity> implements DefaultService<D, E> {

    protected final JpaRepository<E, UUID> repository;

    protected final DefaultConverter converter;

    protected final Class<D> dtoClass;

    protected final Class<E> entityClass;

    public DefaultServiceImpl(JpaRepository<E, UUID> repository, DefaultConverter defaultConverter, Class<D> dtoClass, Class<E> entityClass) {
        this.repository = repository;
        this.converter = defaultConverter;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public D create(D dto) {
        if (dto != null) {
            dto.setId(null);
            E entity = converter.convert(dto, entityClass);
            onCreate(entity);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<D> create(List<D> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            List<E> entities = converter.convert(dtos, entityClass);
            entities = entities.stream().filter(Objects::nonNull).map(this::onCreate).collect(Collectors.toList());
            entities = repository.saveAll(entities);
            repository.flush();
            dtos = converter.convert(entities, dtoClass);
        }
        return dtos;
    }

    @Override
    @Transactional
    public D update(D dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            E entity = converter.convert(dto, entityClass);
            onUpdate(entity);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<D> update(List<D> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            if (dtos.stream().anyMatch(i -> i.getId() == null)) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            List<E> entities = converter.convert(dtos, entityClass);
            onUpdate(entities);
            entities = repository.saveAll(entities);
            repository.flush();
            dtos = converter.convert(entities, dtoClass);
        }
        return dtos;
    }

    @Override
    @Transactional
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
    @Transactional
    public List<D> getByIds(Iterable<UUID> ids) {
        List<D> result = null;
        if (ids != null) {
            List<E> entities = repository.findAllById(ids);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    @Transactional
    public List<D> getAll() {
        List<E> entities = repository.findAll();
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public Page<D> getAll(Pageable pageable) {
        Page<E> entities = repository.findAll(pageable);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id != null) {
            Optional<E> entity = repository.findById(id);
            entity.ifPresent(repository::delete);
        }
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean isExist(UUID id) {
        return repository.existsById(id);
    }

    protected E onCreate(E entity) {
        return entity;
    }

    protected E onUpdate(E entity) {
        E existed = repository
                .findById(entity.getId())
                .orElseThrow(() -> new ClientException(NOT_EXIST_BY_ID));
        return onUpdate(entity, existed);
    }

    protected E onUpdate(E entity, E existed) {
        return entity;
    }

    protected List<E> onUpdate(List<E> entities) {
        List<UUID> ids = entities.stream().map(DefaultEntity::getId).collect(Collectors.toList());
        List<E> existedList = repository.findAllById(ids);
        if (CollectionUtils.isEmpty(existedList)) {
            throw new ClientException(NOT_EXIST_BY_ID);
        }
        Map<UUID, E> existedMap = existedList.stream().collect(Collectors.toMap(DefaultEntity::getId, i -> i));
        entities.forEach(i -> {
            E existed = existedMap.get(i.getId());
            if (existed == null) {
                throw new ClientException(NOT_EXIST_BY_ID);
            }
            onUpdate(i, existed);
        });
        return entities;
    }

}

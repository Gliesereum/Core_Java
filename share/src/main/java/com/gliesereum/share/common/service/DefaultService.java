package com.gliesereum.share.common.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface DefaultService<D extends DefaultDto, E extends DefaultEntity> {

    D create(D dto);

    List<D> create(List<D> dtos);

    D update(D dto);

    List<D> update(List<D> dtos);

    D getById(UUID id);

    List<D> getByIds(Iterable<UUID> ids);

    List<D> getAll();

    Page<D> getAll(Pageable pageable);

    void delete(UUID id);

    boolean isExist(UUID id);
}

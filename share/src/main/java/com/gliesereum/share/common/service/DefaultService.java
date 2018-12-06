package com.gliesereum.share.common.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public interface DefaultService<D extends DefaultDto, E extends DefaultEntity> {

    D create(D dto);

    D update(D dto);

    D getById(UUID id);

    List<D> getAll();

    void delete(UUID id);

    boolean isExist(UUID id);
}

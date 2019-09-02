package com.gliesereum.share.common.service.auditable;

import com.gliesereum.share.common.model.dto.AuditableDefaultDto;
import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AuditableService<D extends AuditableDefaultDto, E extends AuditableDefaultEntity> extends DefaultService<D, E> {

    D getByIdAndObjectState(UUID id, ObjectState objectState);

    List<D> getByIds(Iterable<UUID> ids, ObjectState objectState);

    List<D> getAll(ObjectState objectState);

    Page<D> getAll(ObjectState objectState, Pageable pageable);

    boolean isExist(UUID id, ObjectState objectState);

    long count(ObjectState objectState);

    void delete(D dto);
}

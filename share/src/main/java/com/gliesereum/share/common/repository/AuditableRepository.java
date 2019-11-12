package com.gliesereum.share.common.repository;

import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@NoRepositoryBean
public interface AuditableRepository<T extends AuditableDefaultEntity> extends JpaRepository<T, UUID> {

    Optional<T> findByIdAndObjectState(UUID id, ObjectState objectState);

    List<T> findByIdInAndObjectState(Iterable<UUID> ids, ObjectState objectState);

    List<T> findByObjectState(ObjectState objectState);

    Page<T> findAllByObjectState(ObjectState objectState, Pageable pageable);
    
    List<T> findAllByObjectState(ObjectState objectState);

    boolean existsByIdAndObjectState(UUID id, ObjectState objectState);

    long countByObjectState(ObjectState objectState);

}

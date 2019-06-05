package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseBusinessRepository extends JpaRepository<BaseBusinessEntity, UUID>, RefreshableRepository {

    boolean existsByIdAndCorporationIdInAndObjectState(UUID id, List<UUID> corporationIds, ObjectState objectState);

    List<BaseBusinessEntity> findByCorporationIdInAndObjectState(List<UUID> corporationIds, ObjectState objectState);

    List<BaseBusinessEntity> findByCorporationIdAndObjectState(UUID corporationId, ObjectState objectState);

    BaseBusinessEntity findByIdAndObjectState(UUID id, ObjectState objectState);

    List<BaseBusinessEntity> findByIdInAndObjectState(List<UUID> ids, ObjectState objectState);

    List<BaseBusinessEntity> getAllByObjectState(ObjectState objectState);

    List<BaseBusinessEntity> getAllByIdInAndObjectState(Iterable<UUID> ids, ObjectState objectState);
}

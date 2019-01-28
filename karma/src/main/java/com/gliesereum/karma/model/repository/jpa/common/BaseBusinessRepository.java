package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.BaseBusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseBusinessRepository extends JpaRepository<BaseBusinessEntity, UUID> {


    boolean existsByIdAndCorporationIdIn(UUID id, List<UUID> corporationIds);

    List<BaseBusinessEntity> findByCorporationIdIn(List<UUID> corporationIds);

    List<BaseBusinessEntity> findByCorporationId(UUID corporationId);
}

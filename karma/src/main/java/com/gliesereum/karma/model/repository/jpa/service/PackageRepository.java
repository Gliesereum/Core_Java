package com.gliesereum.karma.model.repository.jpa.service;

import com.gliesereum.karma.model.entity.service.PackageEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PackageRepository extends JpaRepository<PackageEntity, UUID> {

    List<PackageEntity> getByBusinessIdAndObjectState(UUID businessId, ObjectState objectState);

    List<PackageEntity> getAllByObjectState(ObjectState objectState);

    PackageEntity findByIdAndObjectState(UUID id, ObjectState objectState);

    List<PackageEntity> getAllByIdInAndObjectState(Iterable<UUID> ids, ObjectState objectState);

    List<PackageEntity> findAllByBusinessIdIn(List<UUID> businessIds);
}

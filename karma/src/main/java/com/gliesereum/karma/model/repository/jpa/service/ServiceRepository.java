package com.gliesereum.karma.model.repository.jpa.service;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {

    List<ServiceEntity> getAllByObjectState(ObjectState objectState);

    List<ServiceEntity> getAllByServiceTypeAndObjectStateOrderByName(ServiceType type, ObjectState state);

    ServiceEntity findByIdAndObjectState(UUID id, ObjectState objectState);

    List<ServiceEntity> getAllByIdInAndObjectState(Iterable<UUID> ids, ObjectState objectState);
}

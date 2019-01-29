package com.gliesereum.karma.model.repository.jpa.service;

import com.gliesereum.karma.model.entity.service.ServiceClassEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface ServiceClassRepository extends JpaRepository<ServiceClassEntity, UUID> {

    boolean existsById(UUID id);

    List<ServiceClassEntity> findAllByServiceType(ServiceType serviceType);

}

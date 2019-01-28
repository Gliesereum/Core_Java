package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.FilterEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FilterRepository extends JpaRepository<FilterEntity, UUID> {

    List<FilterEntity> findAllByServiceType(ServiceType serviceType);
}

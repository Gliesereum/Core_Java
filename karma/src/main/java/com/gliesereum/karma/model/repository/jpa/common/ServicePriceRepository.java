package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServicePriceRepository extends JpaRepository<ServicePriceEntity, UUID> {

    List<ServicePriceEntity> findAllByCorporationServiceId(UUID id);

}

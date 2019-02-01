package com.gliesereum.karma.model.repository.jpa.service;

import com.gliesereum.karma.model.entity.service.ServicePriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServicePriceRepository extends JpaRepository<ServicePriceEntity, UUID> {

    List<ServicePriceEntity> findAllByBusinessId(UUID id);

}
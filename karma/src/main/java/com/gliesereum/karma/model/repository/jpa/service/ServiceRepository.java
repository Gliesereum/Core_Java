package com.gliesereum.karma.model.repository.jpa.service;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {
}

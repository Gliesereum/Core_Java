package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {
}

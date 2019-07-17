package com.gliesereum.permission.model.repository.jpa.application;

import com.gliesereum.permission.model.entity.application.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {
}

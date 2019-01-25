package com.gliesereum.media.model.repository.jpa;

import com.gliesereum.media.model.entity.UserFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserFileRepository extends JpaRepository<UserFileEntity, UUID> {
}

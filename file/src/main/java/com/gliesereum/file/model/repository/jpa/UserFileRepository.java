package com.gliesereum.file.model.repository.jpa;

import com.gliesereum.file.model.entity.UserFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserFileRepository extends JpaRepository<UserFileEntity, UUID> {
}

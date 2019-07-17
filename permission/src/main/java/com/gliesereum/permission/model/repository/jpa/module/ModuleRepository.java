package com.gliesereum.permission.model.repository.jpa.module;

import com.gliesereum.permission.model.entity.module.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface ModuleRepository extends JpaRepository<ModuleEntity, UUID> {


    ModuleEntity findByUrl(String url);
}

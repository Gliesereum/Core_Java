package com.gliesereum.permission.model.repository.jpa.module;

import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 06/11/2018
 */
@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, UUID> {


    ModuleEntity findByUrl(String url);
}

package com.gliesereum.language.model.repository.jpa;

import com.gliesereum.language.model.entity.PackageEntity;
import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface PackageRepository extends JpaRepository<PackageEntity, UUID>, RefreshableRepository {

    List<PackageEntity> findAllByModule(String module);

    boolean existsByModuleAndIsoKey(String module, String isoKey);
}

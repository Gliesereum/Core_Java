package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface PackageRepository extends JpaRepository<PackageEntity, UUID> {

    List<PackageEntity> getByBusinessId(UUID businessId);
}

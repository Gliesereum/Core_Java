package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.PackageServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PackageServiceRepository extends JpaRepository<PackageServiceEntity, UUID> {

    void deleteAllByPackageIdAndServiceIdIn(UUID packageId, List<UUID> servicePricesIds);
}

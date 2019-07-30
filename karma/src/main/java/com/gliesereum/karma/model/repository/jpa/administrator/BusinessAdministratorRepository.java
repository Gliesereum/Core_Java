package com.gliesereum.karma.model.repository.jpa.administrator;

import com.gliesereum.karma.model.entity.administator.BusinessAdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BusinessAdministratorRepository extends JpaRepository<BusinessAdministratorEntity, UUID> {

    boolean existsByUserIdAndBusinessId(UUID userId, UUID businessId);

    boolean existsByUserIdAndCorporationId(UUID userId, UUID corporationId);

    boolean existsByUserId(UUID userId);

    void deleteByUserIdAndBusinessId(UUID userId, UUID corporationId);

    List<BusinessAdministratorEntity> findAllByBusinessId(UUID businessId);

    List<BusinessAdministratorEntity> findAllByCorporationId(UUID corporationId);

    List<BusinessAdministratorEntity> findAllByUserId(UUID userId);
}
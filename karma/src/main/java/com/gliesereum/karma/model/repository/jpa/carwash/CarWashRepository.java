package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarWashRepository extends JpaRepository<CarWashEntity, UUID> {


    boolean existsByIdAndCorporationIdIn(UUID id, List<UUID> corporationIds);

    List<CarWashEntity> findByCorporationIdIn(List<UUID> corporationIds);

    List<CarWashEntity> findByCorporationId(UUID corporationId);
}

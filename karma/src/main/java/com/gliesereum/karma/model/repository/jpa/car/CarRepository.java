package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarRepository extends JpaRepository<CarEntity, UUID> {

    List<CarEntity> getAllByUserId(UUID id);

    List<CarEntity> getAllByBrandIdIn(List<UUID> brandIds);

    void deleteAllByUserId(UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    Optional<CarEntity> findByIdAndUserId(UUID carId, UUID userId);
}

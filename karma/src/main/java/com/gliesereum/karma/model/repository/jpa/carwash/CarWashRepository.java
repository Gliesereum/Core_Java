package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Repository
public interface CarWashRepository extends JpaRepository<CarWashEntity, UUID> {

    boolean existsByIdAndUserBusinessId(UUID id, UUID userBusinessId);

    List<CarWashEntity> findByUserBusinessId(UUID userBusinessId);
}

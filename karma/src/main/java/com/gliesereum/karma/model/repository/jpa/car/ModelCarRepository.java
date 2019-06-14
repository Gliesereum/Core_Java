package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.ModelCarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ModelCarRepository extends JpaRepository<ModelCarEntity, UUID> {

    List<ModelCarEntity> getAllByBrandId(UUID id);
}

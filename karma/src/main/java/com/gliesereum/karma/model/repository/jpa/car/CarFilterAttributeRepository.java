package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.CarFilterAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarFilterAttributeRepository extends JpaRepository<CarFilterAttributeEntity, UUID> {

    void deleteByCarIdAndFilterAttributeId(UUID idCar, UUID filterAttributeId);
}

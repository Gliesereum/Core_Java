package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.CarServiceClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface CarServiceClassRepository extends JpaRepository<CarServiceClassEntity, UUID> {

    void deleteByCarIdAndAndServiceClassId(UUID idCar, UUID idService);
}

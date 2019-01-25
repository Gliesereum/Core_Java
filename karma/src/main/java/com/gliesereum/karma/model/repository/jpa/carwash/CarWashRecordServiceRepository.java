package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarWashRecordServiceRepository extends JpaRepository<CarWashRecordServiceEntity, UUID> {
}

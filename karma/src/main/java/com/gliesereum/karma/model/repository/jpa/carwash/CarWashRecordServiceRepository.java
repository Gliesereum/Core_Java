package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Repository
public interface CarWashRecordServiceRepository extends JpaRepository<CarWashRecordServiceEntity, UUID> {
}

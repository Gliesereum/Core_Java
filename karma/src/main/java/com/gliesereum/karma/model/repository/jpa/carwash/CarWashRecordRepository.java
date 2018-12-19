package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashRecordRepository extends JpaRepository<CarWashRecordEntity, UUID>{

     List<CarWashRecordEntity> findByStatusRecordAndDateAndWorkingSpaceIdIn(StatusRecord status, LocalDate date, List<UUID> workingSpaceIds);

     List<CarWashRecordEntity> findByBeginTimeGreaterThanEqualAndStatusRecordAndDateAndCarIdIn(LocalTime time, StatusRecord status, LocalDate date, List<UUID> carIds);

     List<CarWashRecordEntity> findByDateAndStatusRecordAndWorkingSpaceId(LocalDate date, StatusRecord status, UUID workingSpaceId);
}

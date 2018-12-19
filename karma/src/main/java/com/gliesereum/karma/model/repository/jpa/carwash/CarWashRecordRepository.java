package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashRecordRepository extends JpaRepository<CarWashRecordEntity, UUID>{

     List<CarWashRecordEntity> findByStatusRecordAndDateAndCarWashId(StatusRecord status, LocalDate date, UUID carWashId);

     List<CarWashRecordEntity> findByBeginTimeGreaterThanEqualAndStatusRecordAndDateAndCarIdIn(Time time, StatusRecord status, LocalDate date, List<UUID> carIds);
}

package com.gliesereum.karma.model.repository.jpa.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarWashRecordRepository extends JpaRepository<CarWashRecordEntity, UUID>{

     List<CarWashRecordEntity> findByStatusRecordAndWorkingSpaceIdInAndBeginBetweenOrderByBegin(StatusRecord status,List<UUID> workingSpaceIds, LocalDateTime from, LocalDateTime to);

     List<CarWashRecordEntity> findByStatusRecordAndCarIdInAndBeginBetween(StatusRecord status, List<UUID> carIds, LocalDateTime from, LocalDateTime to);

     List<CarWashRecordEntity> findByCarWashIdAndStatusRecordAndBeginBetween(UUID carWashId, StatusRecord status,LocalDateTime from, LocalDateTime to);

     List<CarWashRecordEntity> findAllByCarIdIn(List<UUID> ids);
}

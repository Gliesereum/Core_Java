package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseRecordRepository extends JpaRepository<BaseRecordEntity, UUID>{

     List<BaseRecordEntity> findByStatusRecordInAndBusinessIdInAndBeginBetweenOrderByBegin(
             List<StatusRecord> status, List<UUID> businessIds, LocalDateTime from, LocalDateTime to);

     List<BaseRecordEntity> findByStatusRecordInAndTargetIdInAndBeginBetweenOrderByBeginDesc(
             List<StatusRecord> status, List<UUID> targetIds, LocalDateTime from, LocalDateTime to);

     List<BaseRecordEntity> findByBusinessIdAndStatusRecordAndBeginBetween(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to);

     List<BaseRecordEntity> findAllByTargetIdInAndServiceType(List<UUID> ids, ServiceType serviceType);
}

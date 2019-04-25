package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18x
 */
public interface BaseRecordRepository extends JpaRepository<BaseRecordEntity, UUID>, RefreshableRepository {

    List<BaseRecordEntity> findByStatusRecordInAndStatusProcessInAndBusinessIdInAndBeginBetweenOrderByBegin(
            List<StatusRecord> status, List<StatusProcess> processes, List<UUID> businessIds, LocalDateTime from, LocalDateTime to);

    List<BaseRecordEntity> findByStatusRecordInAndStatusProcessInAndTargetIdInAndBeginBetweenOrderByBeginDesc(
            List<StatusRecord> status, List<StatusProcess> processes, List<UUID> targetIds, LocalDateTime from, LocalDateTime to);

    List<BaseRecordEntity> findByBusinessIdAndStatusRecordAndBeginBetween(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to);

    List<BaseRecordEntity> findAllByTargetIdInAndBusinessCategoryId(List<UUID> ids, UUID businessCategoryId);
}

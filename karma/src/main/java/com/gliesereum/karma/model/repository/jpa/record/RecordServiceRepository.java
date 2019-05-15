package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.RecordServiceEntity;
import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface RecordServiceRepository extends JpaRepository<RecordServiceEntity, UUID>, RefreshableRepository {

    List<RecordServiceEntity> findAllByRecordIdIn(List<UUID> recordId);
}

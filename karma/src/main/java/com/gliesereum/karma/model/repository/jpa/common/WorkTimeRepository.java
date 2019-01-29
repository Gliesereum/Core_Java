package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.WorkTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkTimeRepository extends JpaRepository<WorkTimeEntity, UUID> {

    List<WorkTimeEntity> findByObjectId(UUID objectId);

    boolean existsByObjectIdAndDayOfWeek(UUID objectId, DayOfWeek dayOfWeek);
}

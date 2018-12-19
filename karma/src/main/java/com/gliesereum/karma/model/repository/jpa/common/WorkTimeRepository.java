package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.WorkTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface WorkTimeRepository extends JpaRepository<WorkTimeEntity, UUID> {

    List<WorkTimeEntity> findByBusinessServiceId(UUID businessServiceId);
}

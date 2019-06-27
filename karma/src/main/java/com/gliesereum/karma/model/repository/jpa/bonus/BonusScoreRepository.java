package com.gliesereum.karma.model.repository.jpa.bonus;

import com.gliesereum.karma.model.entity.bonus.BonusScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BonusScoreRepository extends JpaRepository<BonusScoreEntity, UUID> {

    Optional<BonusScoreEntity> findByUserId(UUID userId);
}
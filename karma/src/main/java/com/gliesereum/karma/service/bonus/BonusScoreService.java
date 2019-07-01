package com.gliesereum.karma.service.bonus;

import com.gliesereum.karma.model.entity.bonus.BonusScoreEntity;
import com.gliesereum.share.common.model.dto.karma.bonus.BonusScoreDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BonusScoreService extends DefaultService<BonusScoreDto, BonusScoreEntity> {

    BonusScoreDto getOrCreateByUserId(UUID userId);

    BonusScoreDto updateScore(Double updateValue, UUID userId);

    BonusScoreDto create(UUID userId);
}

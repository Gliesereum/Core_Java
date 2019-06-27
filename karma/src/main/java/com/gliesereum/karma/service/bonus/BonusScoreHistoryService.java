package com.gliesereum.karma.service.bonus;

import com.gliesereum.karma.model.entity.bonus.BonusScoreHistoryEntity;
import com.gliesereum.share.common.model.dto.karma.bonus.BonusScoreHistoryDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BonusScoreHistoryService extends DefaultService<BonusScoreHistoryDto, BonusScoreHistoryEntity> {

    BonusScoreHistoryDto addHistory(UUID bonusScoreId, Double value);
}
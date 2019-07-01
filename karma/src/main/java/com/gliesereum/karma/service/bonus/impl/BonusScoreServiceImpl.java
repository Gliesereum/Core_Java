package com.gliesereum.karma.service.bonus.impl;

import com.gliesereum.karma.model.entity.bonus.BonusScoreEntity;
import com.gliesereum.karma.model.repository.jpa.bonus.BonusScoreRepository;
import com.gliesereum.karma.service.bonus.BonusScoreHistoryService;
import com.gliesereum.karma.service.bonus.BonusScoreService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.bonus.BonusScoreDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class BonusScoreServiceImpl extends DefaultServiceImpl<BonusScoreDto, BonusScoreEntity> implements BonusScoreService {

    private static final Class<BonusScoreDto> DTO_CLASS = BonusScoreDto.class;
    private static final Class<BonusScoreEntity> ENTITY_CLASS = BonusScoreEntity.class;

    private final BonusScoreRepository bonusScoreRepository;

    @Autowired
    private BonusScoreHistoryService bonusScoreHistoryService;

    @Autowired
    public BonusScoreServiceImpl(BonusScoreRepository bonusScoreRepository, DefaultConverter defaultConverter) {
        super(bonusScoreRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.bonusScoreRepository = bonusScoreRepository;
    }

    @Override
    @Transactional
    public BonusScoreDto getOrCreateByUserId(UUID userId) {
        BonusScoreDto result = null;
        if (userId != null) {
            Optional<BonusScoreEntity> entity = bonusScoreRepository.findByUserId(userId);
            if (entity.isPresent()) {
                result = converter.convert(entity.get(), dtoClass);
            } else {
                result = create(userId);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public BonusScoreDto updateScore(Double updateValue, UUID userId) {
        BonusScoreDto result = null;
        if (ObjectUtils.allNotNull(userId, updateValue) && (updateValue != 0.0)) {
            BonusScoreDto bonusScore = getOrCreateByUserId(userId);
            bonusScore.setScore(bonusScore.getScore() + updateValue);
            result = super.update(bonusScore);
            bonusScoreHistoryService.addHistory(result.getId(), updateValue);
        }
        return result;
    }

    @Override
    public BonusScoreDto create(UUID userId) {
        BonusScoreDto result = null;
        if (userId != null) {
            BonusScoreEntity entity = new BonusScoreEntity();
            entity.setUserId(userId);
            entity.setScore(0.0);
            entity = bonusScoreRepository.saveAndFlush(entity);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}
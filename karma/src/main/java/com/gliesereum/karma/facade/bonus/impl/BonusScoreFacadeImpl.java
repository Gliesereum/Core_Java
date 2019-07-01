package com.gliesereum.karma.facade.bonus.impl;

import com.gliesereum.karma.facade.bonus.BonusScoreFacade;
import com.gliesereum.karma.service.bonus.BonusScoreService;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class BonusScoreFacadeImpl implements BonusScoreFacade {

    private static final Double SCORE_ON_SIGNUP_WITH_CODE = 50.0;

    @Autowired
    private BonusScoreService bonusScoreService;

    @Override
    public void addScoreBySignupWithCode(ReferralCodeUserDto referralCodeUserDto) {
        if(referralCodeUserDto != null) {
            UUID referrerId = referralCodeUserDto.getReferrerId();
            UUID userId = referralCodeUserDto.getUserId();
            bonusScoreService.updateScore(SCORE_ON_SIGNUP_WITH_CODE, referrerId);
            bonusScoreService.updateScore(SCORE_ON_SIGNUP_WITH_CODE, userId);
        }
    }
}

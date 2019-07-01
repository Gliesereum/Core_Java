package com.gliesereum.karma.facade.bonus;

import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BonusScoreFacade {

    void addScoreBySignupWithCode(ReferralCodeUserDto referralCodeUserDto);
}

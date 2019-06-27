package com.gliesereum.account.facade.referral;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ReferralFacade {

    void signUpWithCode(UUID userId, String referralCode);
}

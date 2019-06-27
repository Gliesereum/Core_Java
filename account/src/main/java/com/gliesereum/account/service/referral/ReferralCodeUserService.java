package com.gliesereum.account.service.referral;

import com.gliesereum.account.model.entity.referral.ReferralCodeUserEntity;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface ReferralCodeUserService extends DefaultService<ReferralCodeUserDto, ReferralCodeUserEntity> {

    ReferralCodeUserDto create(UUID userId, UUID referralCodeId, UUID referrerId);
}
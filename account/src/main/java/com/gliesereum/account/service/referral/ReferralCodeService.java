package com.gliesereum.account.service.referral;

import com.gliesereum.account.model.entity.referral.ReferralCodeEntity;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ReferralCodeService extends DefaultService<ReferralCodeDto, ReferralCodeEntity> {

    ReferralCodeDto getOrCreate(UUID userId);

    ReferralCodeDto getByUserId(UUID userId);

    ReferralCodeDto generate(UUID userId);
}

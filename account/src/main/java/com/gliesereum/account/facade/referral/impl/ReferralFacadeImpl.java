package com.gliesereum.account.facade.referral.impl;

import com.gliesereum.account.facade.notification.SystemNotificationFacade;
import com.gliesereum.account.facade.referral.ReferralFacade;
import com.gliesereum.account.service.referral.ReferralCodeService;
import com.gliesereum.account.service.referral.ReferralCodeUserService;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeDto;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ReferralFacadeImpl implements ReferralFacade {

    @Autowired
    private ReferralCodeService referralCodeService;

    @Autowired
    private ReferralCodeUserService referralCodeUserService;

    @Autowired
    private SystemNotificationFacade systemNotificationFacade;

    @Override
    @Async
    public void signUpWithCode(UUID userId, String referralCode) {
        if ((userId != null) && StringUtils.isNotEmpty(referralCode)) {
            ReferralCodeDto byCode = referralCodeService.getByCode(referralCode);
            if (byCode != null) {
                ReferralCodeUserDto referralCodeUser = referralCodeUserService.create(userId, byCode.getId(), byCode.getUserId());
                systemNotificationFacade.sendSignUpWithCodeNotification(referralCodeUser);
            }
        }
    }

}

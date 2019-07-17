package com.gliesereum.account.facade.notification;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface SystemNotificationFacade {

    void sendCorporationDelete(CorporationDto corporation);

    void sendSignUpWithCodeNotification(ReferralCodeUserDto referralCodeUser);

    void sendUpdateAuthInfoNotification(List<AuthDto> authInfos);

    void updateClientInfo(UserDto user);
}

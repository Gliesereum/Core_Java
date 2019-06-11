package com.gliesereum.account.facade.user.impl;

import com.gliesereum.account.facade.user.UserFacade;
import com.gliesereum.account.service.kyc.KycRequestService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestDto;
import com.gliesereum.share.common.model.dto.account.user.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private CorporationService corporationService;

    @Autowired
    private KycRequestService kycRequestService;

    @Autowired
    private UserPhoneService userPhoneService;

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private UserService userService;

    @Override
    public List<DetailedUserDto> getDetailedByIds(List<UUID> ids) {
        List<DetailedUserDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserDto> users = userService.getByIds(ids);
            if (CollectionUtils.isNotEmpty(users)) {
                List<UUID> userIds = users.stream().map(UserDto::getId).collect(Collectors.toList());
                Map<UUID, List<CorporationDto>> corporationMap = corporationService.getCorporationByUserIds(userIds);
                Map<UUID, List<KycRequestDto>> kycMap = kycRequestService.getAllByUserIdsAndStatuses(ids, Arrays.asList(KycStatus.KYC_PASSED));
                Map<UUID, UserPhoneDto> phoneMap = userPhoneService.getMapByUserIds(userIds);
                Map<UUID, UserEmailDto> emailMap = userEmailService.getMapByUserIds(userIds);
                result = users.stream().map(i -> {
                    DetailedUserDto detailedUser = new DetailedUserDto();
                    detailedUser.setId(i.getId());
                    detailedUser.setUser(i);
                    detailedUser.setPhone(phoneMap.getOrDefault(i.getId(), new UserPhoneDto()).getPhone());
                    detailedUser.setEmail(emailMap.getOrDefault(i.getId(), new UserEmailDto()).getEmail());
                    detailedUser.setCorporations(corporationMap.get(i.getId()));
                    detailedUser.setPassedKycRequests(kycMap.get(i.getId()));
                    return detailedUser;
                }).collect(Collectors.toList());
            }
        }
        return result;
    }
}

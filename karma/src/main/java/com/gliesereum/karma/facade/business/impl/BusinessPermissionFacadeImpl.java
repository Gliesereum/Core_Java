package com.gliesereum.karma.facade.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class BusinessPermissionFacadeImpl implements BusinessPermissionFacade {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private ClientEsService clientEsService;

    @Autowired
    private BaseBusinessService baseBusinessService;


    @Override
    public void checkUserHavePermissionToClient(UUID corporationId, UUID clientId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (ObjectUtils.allNotNull(corporationId, clientId, currentUserId)) {
            checkUserHavePermissionForWorkWithClient(corporationId, currentUserId, currentUserCorporationIds);
            ClientDto client = clientEsService.getClientByUserId(clientId);
            if (client == null) {
                throw new ClientException(CLIENT_NOT_FOUND);
            }
            if (CollectionUtils.isEmpty(client.getCorporationIds()) || !client.getCorporationIds().contains(corporationId.toString())) {
                throw new ClientException(USER_NOT_CLIENT_FOR_BUSINESS);
            }
        }
    }

    @Override
    public void checkCurrentUserHavePermissionToClient(UUID corporationId, UUID clientId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkUserHavePermissionToClient(corporationId, clientId, user.getId(), user.getCorporationIds());
    }

    @Override
    public void checkUserHavePermissionForWorkWithClient(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (ObjectUtils.allNotNull(corporationId, currentUserId)) {
            if (CollectionUtils.isEmpty(currentUserCorporationIds) || !currentUserCorporationIds.contains(corporationId)) {
                if (!workerService.existByUserIdAndCorporationId(currentUserId, corporationId)) {
                    throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
                }
            }
        }
    }

    @Override
    public void checkCurrentUserHavePermissionForWorkWithClient(UUID corporationId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkUserHavePermissionForWorkWithClient(corporationId, user.getId(), user.getCorporationIds());
    }

    @Override
    public void checkUserHavePermissionForWorkWithBusinessClient(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (CollectionUtils.isNotEmpty(businessIds) && (currentUserId != null)) {
            UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
            checkUserHavePermissionForWorkWithClient(corporationId, currentUserId, currentUserCorporationIds);
        }
    }

    @Override
    public void checkCurrentUserHavePermissionForWorkWithBusinessClient(List<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkUserHavePermissionForWorkWithBusinessClient(businessIds, user.getId(), user.getCorporationIds());
    }

    @Override
    public boolean checkUserHavePermissionToViewClientPhone(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, currentUserId)) {
            result = CollectionUtils.isNotEmpty(currentUserCorporationIds) && currentUserCorporationIds.contains(corporationId);
        }
        return result;
    }

    @Override
    public boolean checkCurrentUserHavePermissionToViewClientPhone(UUID corporationId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        return checkUserHavePermissionToViewClientPhone(corporationId, user.getId(), user.getCorporationIds());
    }

    @Override
    public boolean checkUserHavePermissionToViewBusinessClientPhone(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(businessIds) && (currentUserId != null)) {
            UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
            result = checkUserHavePermissionToViewClientPhone(corporationId, currentUserId, currentUserCorporationIds);
        }
        return result;
    }

    @Override
    public boolean checkCurrentUserHavePermissionToBusinessViewClientPhone(Collection<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        return checkUserHavePermissionToViewBusinessClientPhone(businessIds, user.getId(), user.getCorporationIds());
    }

    private UUID getCorporationIdAndCheckEqual(Collection<UUID> businessIds) {
        List<LiteBusinessDto> business = baseBusinessService.getLiteBusinessByIds(businessIds);
        if (CollectionUtils.isEmpty(business)) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        List<UUID> corporationIds = business.stream().map(LiteBusinessDto::getCorporationId).distinct().collect(Collectors.toList());
        if (corporationIds.size() > 1) {
            throw new ClientException(BUSINESS_MUST_BE_FROM_ONE_CORPORATION);
        }
        return corporationIds.get(0);
    }
}

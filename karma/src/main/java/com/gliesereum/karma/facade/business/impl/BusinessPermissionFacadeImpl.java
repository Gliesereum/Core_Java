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
    public boolean currentUserIsOwnerBusiness(Collection<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        return isOwnerBusiness(businessIds, SecurityUtil.getUserId(), SecurityUtil.getUserCorporationIds());
    }

    @Override
    public boolean isOwnerBusiness(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
        return isOwner(corporationId, currentUserId, currentUserCorporationIds);
    }

    @Override
    public boolean currentUserIsOwnerBusiness(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        return isOwnerBusiness(businessId, SecurityUtil.getUserId(), SecurityUtil.getUserCorporationIds());
    }

    @Override
    public void checkCurrentUserIsOwnerBusiness(UUID businessId) {
        if (!currentUserIsOwnerBusiness(businessId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public boolean isOwnerBusiness(UUID businessId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        boolean result = false;
        if (ObjectUtils.allNotNull(businessId, currentUserId, currentUserCorporationIds)) {
            result = baseBusinessService.existByIdAndCorporationIds(businessId, currentUserCorporationIds);
        }
        return result;
    }

    @Override
    public boolean isOwner(UUID corporationId, UUID currentUserId, Collection<UUID> currentUserCorporationIds) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, currentUserId, currentUserCorporationIds)) {
            result = CollectionUtils.isNotEmpty(currentUserCorporationIds) && currentUserCorporationIds.contains(corporationId);
        }
        return result;
    }

    @Override
    public boolean isWorkerBusiness(UUID businessId, UUID userId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(businessId, userId)) {
            result = workerService.existByUserIdAndBusinessId(userId, businessId);
        }
        return result;
    }

    @Override
    public boolean currentUserIsWorkerBusiness(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        return isWorkerBusiness(businessId, SecurityUtil.getUserId());
    }

    @Override
    public boolean isWorker(UUID corporationId, UUID currentUserId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, currentUserId)) {
            result = workerService.existByUserIdAndCorporationId(currentUserId, corporationId);
        }
        return result;
    }

    @Override
    public boolean currentUserIsWorker(UUID corporationId) {
        SecurityUtil.checkUserByBanStatus();
        return isWorker(corporationId, SecurityUtil.getUserId());
    }

    @Override
    public void checkPermissionToBusinessInfo(UUID businessId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (!isOwnerBusiness(businessId, currentUserId, currentUserCorporationIds) && !isWorkerBusiness(businessId, currentUserId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public void checkCurrentUserPermissionToBusinessInfo(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        checkPermissionToBusinessInfo(businessId, SecurityUtil.getUserId(), SecurityUtil.getUserCorporationIds());
    }

    @Override
    public void checkPermissionToBusinessInfo(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
        if (!isOwner(corporationId, currentUserId, currentUserCorporationIds) && !isWorker(corporationId, currentUserId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public void checkCurrentUserPermissionToBusinessInfo(List<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        checkPermissionToBusinessInfo(businessIds, SecurityUtil.getUserId(), SecurityUtil.getUserCorporationIds());
    }

    @Override
    public void checkPermissionToClient(UUID corporationId, UUID clientId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (ObjectUtils.allNotNull(corporationId, clientId, currentUserId)) {
            checkPermissionWorkWithClient(corporationId, currentUserId, currentUserCorporationIds);
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
    public void checkCurrentUserPermissionToClient(UUID corporationId, UUID clientId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkPermissionToClient(corporationId, clientId, user.getId(), user.getCorporationIds());
    }

    @Override
    public void checkPermissionWorkWithClient(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (!isOwner(corporationId, currentUserId, currentUserCorporationIds) && !isWorker(corporationId, currentUserId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public void checkCurrentUserPermissionWorkWithClient(UUID corporationId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkPermissionWorkWithClient(corporationId, user.getId(), user.getCorporationIds());
    }

    @Override
    public void checkPermissionWorkWithClientByBusiness(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        if (CollectionUtils.isNotEmpty(businessIds) && (currentUserId != null)) {
            UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
            checkPermissionWorkWithClient(corporationId, currentUserId, currentUserCorporationIds);
        }
    }

    @Override
    public void checkCurrentUserPermissionWorkWithClientByBusiness(List<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        checkPermissionWorkWithClientByBusiness(businessIds, user.getId(), user.getCorporationIds());
    }

    @Override
    public boolean checkPermissionViewClientPhone(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        return isOwner(corporationId, currentUserId, currentUserCorporationIds);
    }

    @Override
    public boolean checkCurrentUserPermissionViewClientPhone(UUID corporationId) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        return checkPermissionViewClientPhone(corporationId, user.getId(), user.getCorporationIds());
    }

    @Override
    public boolean checkPermissionViewClientPhoneByBusiness(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds) {
        UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
        return checkPermissionViewClientPhone(corporationId, currentUserId, currentUserCorporationIds);
    }

    @Override
    public boolean checkCurrentUserPermissionViewClientPhoneByBusiness(Collection<UUID> businessIds) {
        SecurityUtil.checkUserByBanStatus();
        UserDto user = SecurityUtil.getUserModel();
        return checkPermissionViewClientPhoneByBusiness(businessIds, user.getId(), user.getCorporationIds());
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

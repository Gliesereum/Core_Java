package com.gliesereum.karma.facade.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.service.administrator.BusinessAdministratorService;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.share.common.exception.client.ClientException;
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

    @Autowired
    private BusinessAdministratorService businessAdministratorService;

    @Override
    public void checkPermissionByBusiness(UUID businessId, BusinessPermission businessPermission) {
        if (!isHavePermissionByBusiness(businessId, businessPermission)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public boolean isHavePermissionByBusiness(UUID businessId, BusinessPermission businessPermission) {
        SecurityUtil.checkUserByBanStatus();
        boolean havePermission = false;
        if (ObjectUtils.allNotNull(businessId, businessPermission)) {
            switch (businessPermission) {
                case BUSINESS_ADMINISTRATION:
                    havePermission = currentUserIsOwnerBusiness(businessId);
                    break;
                case WORK_WITH_CHAT:
                case VIEW_BUSINESS_INFO:
                case WORK_WITH_RECORD:
                    havePermission = currentUserIsOwnerBusiness(businessId) || currentUserIdAdminBusiness(businessId) || currentUserIsWorkerBusiness(businessId);
                    break;
                case VIEW_PHONE:
                    havePermission = currentUserIsOwnerBusiness(businessId) || currentUserIdAdminBusiness(businessId);
                    break;
            }
        }
        return havePermission;
    }

    @Override
    public void checkPermissionByBusiness(Collection<UUID> businessIds, BusinessPermission businessPermission) {
        if (!isHavePermissionByBusiness(businessIds, businessPermission)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public boolean isHavePermissionByBusiness(Collection<UUID> businessIds, BusinessPermission businessPermission) {
        SecurityUtil.checkUserByBanStatus();
        UUID corporationId = getCorporationIdAndCheckEqual(businessIds);
        return isHavePermissionByCorporation(corporationId, businessPermission);
    }

    @Override
    public void checkPermissionByCorporation(UUID corporationId, BusinessPermission businessPermission) {
        if (!isHavePermissionByCorporation(corporationId, businessPermission)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    @Override
    public boolean isHavePermissionByCorporation(UUID corporationId, BusinessPermission businessPermission) {
        SecurityUtil.checkUserByBanStatus();
        UUID userId = SecurityUtil.getUserId();
        List<UUID> userCorporationIds = SecurityUtil.getUserCorporationIds();
        boolean havePermission = false;
        if (ObjectUtils.allNotNull(corporationId, businessPermission)) {
            switch (businessPermission) {
                case BUSINESS_ADMINISTRATION:
                    havePermission = isOwner(corporationId, userId, userCorporationIds);
                    break;
                case WORK_WITH_CHAT:
                case WORK_WITH_RECORD:
                case VIEW_BUSINESS_INFO:
                    havePermission = isOwner(corporationId, userId, userCorporationIds) || isAdmin(corporationId, userId) || isWorker(corporationId, userId);
                    break;
                case VIEW_PHONE:
                    havePermission = isOwner(corporationId, userId, userCorporationIds) || isAdmin(corporationId, userId);
                    break;
            }
        }
        return havePermission;
    }

    @Override
    public boolean currentUserIsOwnerBusiness(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        boolean result = false;
        if (businessId != null) {
            result = baseBusinessService.existByIdAndCorporationIds(businessId, SecurityUtil.getUserCorporationIds());
        }
        return result;
    }



    @Override
    public boolean currentUserIsWorkerBusiness(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        boolean result = false;
        if (businessId != null) {
            result = workerService.existByUserIdAndBusinessId(SecurityUtil.getUserId(), businessId);
        }
        return result;
    }

    @Override
    public boolean currentUserIdAdminBusiness(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        boolean result = false;
        if (businessId != null) {
            result = businessAdministratorService.existByUserIdBusinessId(SecurityUtil.getUserId(), businessId);
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
    public boolean isWorker(UUID corporationId, UUID currentUserId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, currentUserId)) {
            result = workerService.existByUserIdAndCorporationId(currentUserId, corporationId);
        }
        return result;
    }

    @Override
    public boolean isAdmin(UUID corporationId, UUID currentUserId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, currentUserId)) {
            result = businessAdministratorService.existByUserIdCorporationId(corporationId, currentUserId);
        }
        return result;
    }

    @Override
    public void checkCurrentUserPermissionToClient(UUID corporationId, UUID clientId) {
        SecurityUtil.checkUserByBanStatus();
        if (corporationId != null) {
            UUID userId = SecurityUtil.getUserId();
            List<UUID> userCorporationIds = SecurityUtil.getUserCorporationIds();
            if (!isOwner(corporationId, userId, userCorporationIds) && !isWorker(corporationId, userId)) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
            }
            ClientDto client = clientEsService.getClientByUserId(clientId);
            if (client == null) {
                throw new ClientException(CLIENT_NOT_FOUND);
            }
            if (CollectionUtils.isEmpty(client.getCorporationIds()) || !client.getCorporationIds().contains(corporationId.toString())) {
                throw new ClientException(USER_NOT_CLIENT_FOR_BUSINESS);
            }
        }
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

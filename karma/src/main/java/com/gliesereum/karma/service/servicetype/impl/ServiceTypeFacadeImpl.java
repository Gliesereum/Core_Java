package com.gliesereum.karma.service.servicetype.impl;

import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.exception.client.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_ACTION_SERVICE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ServiceTypeFacadeImpl implements ServiceTypeFacade {

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @Override
    public boolean currentUserHavePermissionToAction(UUID businessId) {
        boolean result = false;
        if (businessId != null) {
            result = baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(businessId);
        }
        return result;
    }

    @Override
    public void throwExceptionIfUserDontHavePermissionToAction(UUID businessId) {
        if (!currentUserHavePermissionToAction(businessId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_SERVICE);
        }
    }

    @Override
    public boolean currentUserHavePermissionToAction(UUID businessCategoryId, UUID businessId) {
        boolean result = false;
        if (businessCategoryId != null) {
            switch (businessCategoryService.checkAndGetType(businessCategoryId)) {
                case CAR:
                    result = baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(businessId);
                    break;
            }
        }
        return result;
    }

    @Override
    public void throwExceptionIfUserDontHavePermissionToAction(UUID businessCategoryId, UUID businessId) {
        if (!currentUserHavePermissionToAction(businessCategoryId, businessId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_SERVICE);
        }
    }
}

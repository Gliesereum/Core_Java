package com.gliesereum.karma.service.servicetype.impl;

import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_ACTION_SERVICE;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-13
 */

@Service
public class ServiceTypeFacadeImpl implements ServiceTypeFacade {

    @Autowired
    private CarWashService carWashService;

    @Override
    public boolean currentUserHavePermissionToAction(ServiceType serviceType, UUID businessServiceId) {
        boolean result = false;
        if (serviceType != null) {
            switch (serviceType) {
                case CAR_WASH:
                    result = carWashService.currentUserHavePermissionToAction(businessServiceId);
                    break;
            }
        }
        return result;
    }

    @Override
    public void throwExceptionIfUserDontHavePermissionToAction(ServiceType serviceType, UUID businessServiceId) {
        if (!currentUserHavePermissionToAction(serviceType, businessServiceId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_SERVICE);
        }
    }
}

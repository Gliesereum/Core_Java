package com.gliesereum.karma.facade.client.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.service.client.ClientService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.BUSINESS_IDS_OR_CORPORATION_IDS_SHOUT_BE_FULL;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ClientFacadeImpl implements ClientFacade {

    @Autowired
    private ClientEsService clientEsService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Override
    public Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids, Collection<UUID> businessIds) {
        Map<UUID, ClientDto> result = clientService.getClientMapByIds(ids);
        if (MapUtils.isNotEmpty(result) && !businessPermissionFacade.isHavePermissionByBusiness(businessIds, BusinessPermission.VIEW_PHONE)) {
            hidePhone(result.values());
        }
        return result;
    }

    @Override
    public Page<ClientDto> getCustomersByBusinessIdsOrCorporationId(List<UUID> businessIds, UUID corporationId, Integer page, Integer size, String query) {
        Page<ClientDto> result = null;
        boolean viewPhoneCorporation = true;
        boolean viewPhoneBusiness = true;

        if (CollectionUtils.isNotEmpty(businessIds)) {
            businessPermissionFacade.checkPermissionByBusiness(businessIds, BusinessPermission.VIEW_BUSINESS_INFO);
            viewPhoneBusiness = businessPermissionFacade.isHavePermissionByBusiness(businessIds, BusinessPermission.VIEW_PHONE);
        } else {
            if (corporationId != null) {
                businessPermissionFacade.checkPermissionByCorporation(corporationId, BusinessPermission.VIEW_BUSINESS_INFO);
                viewPhoneCorporation = businessPermissionFacade.isHavePermissionByCorporation(corporationId, BusinessPermission.VIEW_PHONE);
            } else {
                throw new ClientException(BUSINESS_IDS_OR_CORPORATION_IDS_SHOUT_BE_FULL);
            }
        }
        result = clientEsService.getClientsByBusinessIdsOrCorporationIdAndQuery(query, businessIds, corporationId, page, size);
        if ((result != null) && (!viewPhoneBusiness || !viewPhoneCorporation)) {
            hidePhone(result.getContent());
        }
        return result;
    }

    private void hidePhone(Collection<ClientDto> clients) {
        if (CollectionUtils.isNotEmpty(clients)) {
            clients.forEach(i -> {
                String phone = i.getPhone();
                String editedPhone = phone.substring(0, 5) + "***" + phone.substring(8);
                i.setPhone(editedPhone);
            });
        }
    }
}

package com.gliesereum.karma.facade.client.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.facade.client.ClientFacade;
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

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ClientFacadeImpl implements ClientFacade {

    @Autowired
    private ClientEsService clientEsService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Override
    public Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids, Collection<UUID> businessIds) {
        Map<UUID, ClientDto> result = clientEsService.getClientMapByIds(ids);
        if (MapUtils.isNotEmpty(result) && businessPermissionFacade.checkCurrentUserHavePermissionToBusinessViewClientPhone(businessIds)) {
            hidePhone(result.values());
        }
        return result;
    }

    @Override
    public Page<ClientDto> getCustomersByBusinessIds(List<UUID> ids, Integer page, Integer size) {
        Page<ClientDto> result = null;
        if (size == null) size = 100;
        if (page == null) page = 0;
        if (CollectionUtils.isNotEmpty(ids)) {
            businessPermissionFacade.checkCurrentUserHavePermissionForWorkWithBusinessClient(ids);
            result = clientEsService.getClientsByBusinessIds(ids, page, size);
            if ((result != null) && !businessPermissionFacade.checkCurrentUserHavePermissionToBusinessViewClientPhone(ids)) {
                hidePhone(result.getContent());
            }
        }
        return result;
    }

    @Override
    public Page<ClientDto> getAllCustomersByCorporationId(UUID id, Integer page, Integer size, String query) {
        Page<ClientDto> result = null;
        if (id != null) {
            businessPermissionFacade.checkCurrentUserHavePermissionForWorkWithClient(id);
            if (size == null) size = 100;
            if (page == null) page = 0;
            result = clientEsService.getClientsByCorporationIdAndAutocompleteQuery(query, id, page, size);
            if ((result != null) && !businessPermissionFacade.checkCurrentUserHavePermissionToViewClientPhone(id)) {
                hidePhone(result.getContent());
            }
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

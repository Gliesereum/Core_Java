package com.gliesereum.karma.service.es;

import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientEsService {

    void addNewClient(PublicUserDto user, UUID businessId);

    void addNewClient(UserDto user, UUID businessId);

    void updateClientInfo(UserDto user);

    Page<ClientDto> getClientsByBusinessIdsOrCorporationIdAndQuery(String query, List<UUID> businessIds, UUID corporationId, Integer page, Integer size);
}

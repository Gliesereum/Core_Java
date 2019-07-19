package com.gliesereum.karma.service.es;

import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientEsService {

    Page<ClientDto> getClientsByBusinessIds(List<UUID> businessIds, Integer page, Integer size);

    Page<ClientDto> getClientsByCorporationIdAndAutocompleteQuery(String query, UUID corporationId, Integer page, Integer size);

    void addNewClient(PublicUserDto user, UUID businessId);

    void addNewClient(UserDto user, UUID businessId);

    void updateClientInfo(UserDto user);

    ClientDto getClientByUserId(UUID userId);

    Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids);

    List<ClientDto> getClientByIds(Collection<UUID> ids);
}

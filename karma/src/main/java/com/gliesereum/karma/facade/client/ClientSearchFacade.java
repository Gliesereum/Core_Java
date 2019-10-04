package com.gliesereum.karma.facade.client;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClientSearchFacade {
    
    Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids, Collection<UUID> businessIds);
    
    Page<ClientDocument> getCustomersByBusinessIdsOrCorporationId(List<UUID> businessIds, UUID corporationId, Integer page, Integer size, String query);
    
}

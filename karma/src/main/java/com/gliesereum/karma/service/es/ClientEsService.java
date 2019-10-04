package com.gliesereum.karma.service.es;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientEsService {
    
    ClientDocument index(ClientDto client);
    
    List<ClientDocument> index(List<ClientDto> client);
    
    Page<ClientDocument> getClientsByBusinessIdsOrCorporationIdAndQuery(String query, List<UUID> businessIds, UUID corporationId, Integer page, Integer size);
}

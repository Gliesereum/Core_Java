package com.gliesereum.karma.service.es;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientEsService {

    Page<ClientDocument> getClientsByBusinessIds(List<UUID> businessIds, int page, int size);

    Page<ClientDocument> getClientsByCorporationIdsAndAutocompleteQuery(String query, List<UUID> corporationIds, int page, int size);

    void addNewClient(PublicUserDto user, UUID businessId);

    void addNewClient(UserDto user, UUID businessId);

    ClientDocument getClientByUserId(UUID userId);
}

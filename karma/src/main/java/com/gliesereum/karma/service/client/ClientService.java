package com.gliesereum.karma.service.client;

import com.gliesereum.karma.model.entity.client.ClientEntity;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.service.auditable.AuditableService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ClientService extends AuditableService<ClientDto, ClientEntity> {

    Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids);

    List<ClientDto> getClientByIds(Collection<UUID> ids);

    ClientDto getByUserId(UUID userId);
}

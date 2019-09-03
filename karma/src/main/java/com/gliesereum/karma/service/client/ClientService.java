package com.gliesereum.karma.service.client;

import com.gliesereum.karma.model.entity.client.ClientEntity;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.service.auditable.AuditableService;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ClientService extends AuditableService<ClientDto, ClientEntity> {
}

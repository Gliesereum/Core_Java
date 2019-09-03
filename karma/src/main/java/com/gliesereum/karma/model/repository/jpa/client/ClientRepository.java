package com.gliesereum.karma.model.repository.jpa.client;

import com.gliesereum.karma.model.entity.client.ClientEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ClientRepository extends AuditableRepository<ClientEntity> {

    List<ClientEntity> findAllByBusinessIdsInAndObjectState(List<UUID> businessIds, ObjectState objectState);


}

package com.gliesereum.karma.model.repository.jpa.client;

import ch.qos.logback.core.net.server.Client;
import com.gliesereum.karma.model.entity.client.ClientEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ClientRepository extends AuditableRepository<ClientEntity> {

    @Query("SELECT c FROM ClientEntity c JOIN c.businessIds b WHERE (b IN :businessIds) AND c.objectState = :objectState")
    List<ClientEntity> findAllByBusinessIdsInAndObjectState(@Param("businessIds") List<UUID> businessIds,
                                                            @Param("objectState") ObjectState objectState);

    @Query("SELECT c FROM ClientEntity c JOIN c.corporationIds co WHERE (co IN :corporationIds) AND c.objectState = :objectSate")
    List<Client> findAllByCorporationIdsAndObjectState(@Param("corporationIds") List<UUID> corporationIds,
                                                       @Param("objectState") ObjectState objectState);
}

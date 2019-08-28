package com.gliesereum.karma.model.repository.jpa.agent;

import com.gliesereum.karma.model.entity.agent.AgentEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AgentRepository extends AuditableRepository<AgentEntity> {

    boolean existsByUserIdAndActiveAndObjectState(UUID userId, Boolean active, ObjectState objectState);

    boolean existsByUserIdAndObjectState(UUID userId, ObjectState objectState);
}

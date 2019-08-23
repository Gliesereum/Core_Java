package com.gliesereum.karma.service.audit;

import com.gliesereum.karma.model.entity.agent.AgentEntity;
import com.gliesereum.share.common.model.dto.karma.agent.AgentDto;
import com.gliesereum.share.common.service.auditable.AuditableService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AgentService extends AuditableService<AgentDto, AgentEntity> {

    boolean existByUserIdAndActive(UUID userId);

    AgentDto createRequest(UUID userId);
}

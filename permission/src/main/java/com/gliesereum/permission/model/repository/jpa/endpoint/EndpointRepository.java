package com.gliesereum.permission.model.repository.jpa.endpoint;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import com.gliesereum.share.common.model.dto.base.enumerated.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface EndpointRepository extends JpaRepository<EndpointEntity, UUID> {

    EndpointEntity findByUrlAndModuleId(String url, UUID moduleId);

    EndpointEntity findByUrlAndModuleIdAndMethod(String url, UUID moduleId, Method method);
}

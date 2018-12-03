package com.gliesereum.permission.model.repository.jpa.endpoint;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 06/11/2018
 */
@Repository
public interface EndpointRepository extends JpaRepository<EndpointEntity, UUID> {
}

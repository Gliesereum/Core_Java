package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.CorporationEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */

public interface CorporationEmployeeRepository extends JpaRepository<CorporationEmployeeEntity, UUID> {

    List<CorporationEmployeeEntity> findByCorporationId(UUID id);

}

package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserCorporationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */

public interface UserCorporationRepository extends JpaRepository<UserCorporationEntity, UUID> {

    List<UserCorporationEntity> findByUserIdAndCorporationIdIn(UUID userId, List<UUID> corporationIds);

}

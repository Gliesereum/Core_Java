package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserCorporationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Repository
public interface UserCorporationRepository extends JpaRepository<UserCorporationEntity, UUID> {

    UserCorporationEntity findByUserIdAndCorporationId(UUID userId, UUID corporationId);

}
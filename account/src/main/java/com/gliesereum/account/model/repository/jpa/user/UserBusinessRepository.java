package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserBusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Repository
public interface UserBusinessRepository extends JpaRepository<UserBusinessEntity, UUID> {

    UserBusinessEntity findByUserIdAndBusinessId(UUID userId, UUID businessId);

}

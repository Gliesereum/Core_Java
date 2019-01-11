package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.DepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
@Repository
public interface DepositoryRepository extends JpaRepository<DepositoryEntity, UUID> {

    List<DepositoryEntity> findByOwnerId(UUID ownerId);
}

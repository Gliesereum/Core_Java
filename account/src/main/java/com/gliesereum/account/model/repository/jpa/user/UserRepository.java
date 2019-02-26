package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    //TODO: KYC REFACTOR
   //List<UserEntity> findByKycStatus(KYCStatus status);
}

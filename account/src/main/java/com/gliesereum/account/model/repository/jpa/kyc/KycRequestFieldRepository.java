package com.gliesereum.account.model.repository.jpa.kyc;

import com.gliesereum.account.model.entity.kyc.KycRequestFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Repository
public interface KycRequestFieldRepository extends JpaRepository<KycRequestFieldEntity, UUID> {

    void deleteAllByKycRequestId(UUID kycRequestId);
}

package com.gliesereum.account.model.repository.jpa.kyc;

import com.gliesereum.account.model.entity.kyc.KycRequestFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface KycRequestFieldRepository extends JpaRepository<KycRequestFieldEntity, UUID> {

    void deleteAllByKycRequestId(UUID kycRequestId);
}

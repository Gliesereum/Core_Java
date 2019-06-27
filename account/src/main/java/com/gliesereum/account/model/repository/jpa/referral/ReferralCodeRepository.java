package com.gliesereum.account.model.repository.jpa.referral;

import com.gliesereum.account.model.entity.referral.ReferralCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ReferralCodeRepository extends JpaRepository<ReferralCodeEntity, UUID> {

    ReferralCodeEntity findByUserId(UUID userId);

    ReferralCodeEntity findByCode(String code);
}

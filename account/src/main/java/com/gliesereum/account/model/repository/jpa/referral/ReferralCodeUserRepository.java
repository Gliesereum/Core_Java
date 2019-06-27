package com.gliesereum.account.model.repository.jpa.referral;

import com.gliesereum.account.model.entity.referral.ReferralCodeUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ReferralCodeUserRepository extends JpaRepository<ReferralCodeUserEntity, UUID> {
}

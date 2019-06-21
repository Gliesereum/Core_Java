package com.gliesereum.account.model.repository.jpa.kyc;

import com.gliesereum.account.model.entity.kyc.KycFieldEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KycFieldRepository extends JpaRepository<KycFieldEntity, UUID> {

    List<KycFieldEntity> findByRequestType(KycRequestType requestType);
}

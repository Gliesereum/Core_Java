package com.gliesereum.account.service.kyc;

import com.gliesereum.account.model.entity.kyc.KycRequestFieldEntity;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestFieldDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;


/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KycRequestFieldService extends DefaultService<KycRequestFieldDto, KycRequestFieldEntity> {

    void deleteAllByKycRequestId(UUID kycRequestId);
}

package com.gliesereum.account.service.kyc;

import com.gliesereum.account.model.entity.kyc.KycFieldEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.kyc.KycFieldDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KycFieldService extends DefaultService<KycFieldDto, KycFieldEntity> {

    List<KycFieldDto> getByType(KycRequestType requestType);
}

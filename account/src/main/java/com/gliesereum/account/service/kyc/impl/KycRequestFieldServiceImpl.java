package com.gliesereum.account.service.kyc.impl;

import com.gliesereum.account.model.entity.kyc.KycRequestFieldEntity;
import com.gliesereum.account.model.repository.jpa.kyc.KycRequestFieldRepository;
import com.gliesereum.account.service.kyc.KycRequestFieldService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestFieldDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class KycRequestFieldServiceImpl extends DefaultServiceImpl<KycRequestFieldDto, KycRequestFieldEntity> implements KycRequestFieldService {

    private static final Class<KycRequestFieldDto> DTO_CLASS = KycRequestFieldDto.class;
    private static final Class<KycRequestFieldEntity> ENTITY_CLASS = KycRequestFieldEntity.class;

    private final KycRequestFieldRepository kycRequestFieldRepository;

    public KycRequestFieldServiceImpl(KycRequestFieldRepository kycRequestFieldRepository, DefaultConverter defaultConverter) {
        super(kycRequestFieldRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.kycRequestFieldRepository = kycRequestFieldRepository;
    }

    @Override
    @Transactional
    public void deleteAllByKycRequestId(UUID kycRequestId) {
        if (kycRequestId != null) {
            kycRequestFieldRepository.deleteAllByKycRequestId(kycRequestId);
        }
    }
}

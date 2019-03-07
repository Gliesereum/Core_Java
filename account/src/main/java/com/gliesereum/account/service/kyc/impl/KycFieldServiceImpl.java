package com.gliesereum.account.service.kyc.impl;

import com.gliesereum.account.model.entity.kyc.KycFieldEntity;
import com.gliesereum.account.model.repository.jpa.kyc.KycFieldRepository;
import com.gliesereum.account.service.kyc.KycFieldService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.kyc.KycFieldDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class KycFieldServiceImpl extends DefaultServiceImpl<KycFieldDto, KycFieldEntity> implements KycFieldService {

    private static final Class<KycFieldDto> DTO_CLASS = KycFieldDto.class;
    private static final Class<KycFieldEntity> ENTITY_CLASS = KycFieldEntity.class;

    private final KycFieldRepository kycFieldRepository;

    @Autowired
    public KycFieldServiceImpl(KycFieldRepository kycFieldRepository, DefaultConverter defaultConverter) {
        super(kycFieldRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.kycFieldRepository = kycFieldRepository;
    }

    @Override
    public List<KycFieldDto> getByType(KycRequestType requestType) {
        List<KycFieldDto> result = null;
        if (requestType == null) {
            result = super.getAll();
        } else {
            List<KycFieldEntity> entities = kycFieldRepository.findByRequestType(requestType);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}

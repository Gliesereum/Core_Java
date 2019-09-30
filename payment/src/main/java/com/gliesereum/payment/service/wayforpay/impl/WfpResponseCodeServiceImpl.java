package com.gliesereum.payment.service.wayforpay.impl;

import com.gliesereum.payment.model.entity.wayforpay.WfpResponseCodeEntity;
import com.gliesereum.payment.model.repository.jpa.wayforpay.WfpResponseCodeRepository;
import com.gliesereum.payment.service.wayforpay.WfpResponseCodeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpResponseCodeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @since 9/5/19
 */
@Slf4j
@Service
public class WfpResponseCodeServiceImpl extends DefaultServiceImpl<WfpResponseCodeDto, WfpResponseCodeEntity> implements WfpResponseCodeService {

    private static final Class<WfpResponseCodeDto> DTO_CLASS = WfpResponseCodeDto.class;
    private static final Class<WfpResponseCodeEntity> ENTITY_CLASS = WfpResponseCodeEntity.class;

    private final WfpResponseCodeRepository wfpResponseCodeRepository;

    @Autowired
    public WfpResponseCodeServiceImpl(WfpResponseCodeRepository wfpResponseCodeRepository, DefaultConverter defaultConverter) {
        super(wfpResponseCodeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.wfpResponseCodeRepository = wfpResponseCodeRepository;
    }

    @Override
    public WfpResponseCodeDto getByCode(Integer code) {
        WfpResponseCodeEntity entity =  wfpResponseCodeRepository.getByReasonCode(code);
        return converter.convert(entity, dtoClass);
    }
}
package com.gliesereum.account.service.referral.impl;

import com.gliesereum.account.model.entity.referral.ReferralCodeEntity;
import com.gliesereum.account.model.repository.jpa.referral.ReferralCodeRepository;
import com.gliesereum.account.service.referral.ReferralCodeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ReferralCodeServiceImpl extends DefaultServiceImpl<ReferralCodeDto, ReferralCodeEntity> implements ReferralCodeService {

    private static final Class<ReferralCodeEntity> ENTITY_CLASS = ReferralCodeEntity.class;
    private static final Class<ReferralCodeDto> DTO_CLASS = ReferralCodeDto.class;

    private final ReferralCodeRepository referralCodeRepository;

    @Autowired
    public ReferralCodeServiceImpl(ReferralCodeRepository referralCodeRepository, DefaultConverter defaultConverter) {
        super(referralCodeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.referralCodeRepository = referralCodeRepository;
    }

    @Override
    public ReferralCodeDto getOrCreate(UUID userId) {
        ReferralCodeDto result = null;
        if (userId != null) {
            result = getByUserId(userId);
            if (result == null) {
                result = generate(userId);
            }
        }
        return result;
    }

    @Override
    public ReferralCodeDto generate(UUID userId) {
        ReferralCodeDto result = null;
        if (userId != null) {
            String id = userId.toString();
            String code = id.substring(0, id.indexOf('-'));
            ReferralCodeDto referralCode = new ReferralCodeDto();
            referralCode.setCode(code);
            referralCode.setUserId(userId);
            result = super.create(referralCode);
        }
        return result;
    }

    @Override
    public ReferralCodeDto getByUserId(UUID userId) {
        ReferralCodeDto result = null;
        if (userId != null) {
            ReferralCodeEntity entity = referralCodeRepository.findByUserId(userId);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public ReferralCodeDto getByCode(String code) {
        ReferralCodeDto result = null;
        if (StringUtils.isNotEmpty(code)) {
            ReferralCodeEntity entity = referralCodeRepository.findByCode(code);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}

package com.gliesereum.account.service.referral.impl;

import com.gliesereum.account.model.entity.referral.ReferralCodeUserEntity;
import com.gliesereum.account.model.repository.jpa.referral.ReferralCodeUserRepository;
import com.gliesereum.account.service.referral.ReferralCodeUserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class ReferralCodeUserServiceImpl extends DefaultServiceImpl<ReferralCodeUserDto, ReferralCodeUserEntity> implements ReferralCodeUserService {

    private static final Class<ReferralCodeUserDto> DTO_CLASS = ReferralCodeUserDto.class;
    private static final Class<ReferralCodeUserEntity> ENTITY_CLASS = ReferralCodeUserEntity.class;

    private final ReferralCodeUserRepository referralCodeUserRepository;

    @Autowired
    public ReferralCodeUserServiceImpl(ReferralCodeUserRepository referralCodeUserRepository, DefaultConverter defaultConverter) {
        super(referralCodeUserRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.referralCodeUserRepository = referralCodeUserRepository;
    }

    @Override
    public ReferralCodeUserDto create(UUID userId, UUID referralCodeId, UUID referrerId) {
        ReferralCodeUserDto result = null;
        if (ObjectUtils.allNotNull(userId, referralCodeId)) {
            ReferralCodeUserEntity entity = new ReferralCodeUserEntity();
            entity.setUserId(userId);
            entity.setReferralCodeId(referralCodeId);
            entity.setReferrerId(referrerId);
            entity = referralCodeUserRepository.saveAndFlush(entity);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}
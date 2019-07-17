package com.gliesereum.karma.service.pincode.impl;

import com.gliesereum.karma.model.entity.pincode.UserPinCodeEntity;
import com.gliesereum.karma.model.repository.jpa.pincode.UserPinCodeRepository;
import com.gliesereum.karma.service.pincode.UserPinCodeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.pincode.UserPinCodeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class UserPinCodeServiceImpl extends DefaultServiceImpl<UserPinCodeDto, UserPinCodeEntity> implements UserPinCodeService {

    private static final Class<UserPinCodeDto> DTO_CLASS = UserPinCodeDto.class;
    private static final Class<UserPinCodeEntity> ENTITY_CLASS = UserPinCodeEntity.class;

    private final UserPinCodeRepository userPinCodeRepository;

    @Autowired
    public UserPinCodeServiceImpl(UserPinCodeRepository userPinCodeRepository, DefaultConverter defaultConverter) {
        super(userPinCodeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userPinCodeRepository = userPinCodeRepository;
    }

    @Override
    public UserPinCodeDto getByUserId(UUID userId) {
        UserPinCodeDto result = null;
        if (userId != null) {
            UserPinCodeEntity entity = userPinCodeRepository.findByUserId(userId);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public UserPinCodeDto save(UserPinCodeDto userPinCode) {
        UserPinCodeDto result = null;
        if (userPinCode != null) {
            UserPinCodeEntity entity = userPinCodeRepository.findByUserId(userPinCode.getUserId());
            if (entity == null) {
                entity = new UserPinCodeEntity();
                entity.setUserId(userPinCode.getUserId());
            }
            entity.setPinCode(userPinCode.getPinCode());
            entity = userPinCodeRepository.saveAndFlush(entity);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}
package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.account.model.repository.jpa.user.UserRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.KFCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Service
public class UserServiceImpl extends DefaultServiceImpl<UserDto, UserEntity> implements UserService {

    @Autowired
    private UserEmailService emailService;

    @Autowired
    private UserPhoneService phoneService;

    private static final Class<UserDto> DTO_CLASS = UserDto.class;
    private static final Class<UserEntity> ENTITY_CLASS = UserEntity.class;

    public UserServiceImpl(UserRepository userRepository, DefaultConverter defaultConverter) {
        super(userRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            emailService.deleteByUserId(id);
            phoneService.deleteByUserId(id);
            super.delete(id);
        }
    }

    @Override
    public UserDto create(UserDto dto) {
        if (dto != null) {
            dto.setVerifiedStatus(VerifiedStatus.UNVERIFIED);
            dto.setBanStatus(BanStatus.UNBAN);
            dto.setKfcStatus(KFCStatus.KFC_NOT_PASSED);
          return super.create(dto);
        }
        return null;
    }

}

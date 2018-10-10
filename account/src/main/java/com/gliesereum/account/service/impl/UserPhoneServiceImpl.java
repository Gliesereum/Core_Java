package com.gliesereum.account.service.impl;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.account.model.repository.UserPhoneRepository;
import com.gliesereum.account.service.UserPhoneService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Service
public class UserPhoneServiceImpl extends DefaultServiceImpl<UserPhoneDto, UserPhoneEntity> implements UserPhoneService {

    @Autowired
    private UserPhoneRepository repository;

    private static final Class<UserPhoneDto> DTO_CLASS = UserPhoneDto.class;
    private static final Class<UserPhoneEntity> ENTITY_CLASS = UserPhoneEntity.class;

    public UserPhoneServiceImpl(UserPhoneRepository userPhoneRepository, DefaultConverter defaultConverter) {
        super(userPhoneRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            repository.deleteUserPhoneEntitiesByUserId(id);
        }
    }
}

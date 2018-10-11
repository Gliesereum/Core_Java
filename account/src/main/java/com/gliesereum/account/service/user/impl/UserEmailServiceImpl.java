package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.account.model.repository.jpa.user.UserEmailRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Service
public class UserEmailServiceImpl extends DefaultServiceImpl<UserEmailDto, UserEmailEntity> implements UserEmailService {

    @Autowired
    private UserEmailRepository repository;

    private static final Class<UserEmailDto> DTO_CLASS = UserEmailDto.class;
    private static final Class<UserEmailEntity> ENTITY_CLASS = UserEmailEntity.class;

    public UserEmailServiceImpl(UserEmailRepository userEmailRepository, DefaultConverter defaultConverter) {
        super(userEmailRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


    @Override
    public void deleteByUserId(UUID id) {
        if(id != null){
          repository.deleteUserEmailEntityByUserId(id);
        }
    }
}

package com.gliesereum.account.service.impl;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.account.model.repository.UserRepository;
import com.gliesereum.account.service.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Service
public class UserServiceImpl extends DefaultServiceImpl<UserDto, UserEntity> implements UserService {

    private static final Class<UserDto> DTO_CLASS = UserDto.class;
    private static final Class<UserEntity> ENTITY_CLASS = UserEntity.class;

    public UserServiceImpl(UserRepository userRepository, DefaultConverter defaultConverter) {
        super(userRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


}

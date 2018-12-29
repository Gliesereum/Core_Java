package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserBusinessEntity;
import com.gliesereum.account.model.repository.jpa.user.UserBusinessRepository;
import com.gliesereum.account.service.user.UserBusinessService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.UserBusinessDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Slf4j
@Service
public class UserBusinessServiceImpl extends DefaultServiceImpl<UserBusinessDto, UserBusinessEntity> implements UserBusinessService {

    private static final Class<UserBusinessDto> DTO_CLASS = UserBusinessDto.class;
    private static final Class<UserBusinessEntity> ENTITY_CLASS = UserBusinessEntity.class;

    private final UserBusinessRepository userBusinessRepository;

    @Autowired
    public UserBusinessServiceImpl(UserBusinessRepository userBusinessRepository, DefaultConverter converter) {
        super(userBusinessRepository, converter, DTO_CLASS, ENTITY_CLASS);
        this.userBusinessRepository = userBusinessRepository;
    }

    @Override
    public UserBusinessDto getByUserIdAndBusinessId(UUID userId, UUID businessId) {
        UserBusinessEntity entity = userBusinessRepository.findByUserIdAndBusinessId(userId, businessId);
        return converter.convert(entity, dtoClass);
    }
}

package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserCorporationEntity;
import com.gliesereum.account.model.repository.jpa.user.UserCorporationRepository;
import com.gliesereum.account.service.user.UserCorporationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class UserCorporationServiceImpl extends DefaultServiceImpl<UserCorporationDto, UserCorporationEntity> implements UserCorporationService {

    private static final Class<UserCorporationDto> DTO_CLASS = UserCorporationDto.class;
    private static final Class<UserCorporationEntity> ENTITY_CLASS = UserCorporationEntity.class;

    private final UserCorporationRepository userCorporationRepository;

    @Autowired
    public UserCorporationServiceImpl(UserCorporationRepository userCorporationRepository, DefaultConverter converter) {
        super(userCorporationRepository, converter, DTO_CLASS, ENTITY_CLASS);
        this.userCorporationRepository = userCorporationRepository;
    }

    @Override
    public List<UserCorporationDto> getAllByUserIdAndCorporationId(UUID userId, List<UUID> corporationIds) {
        List<UserCorporationEntity> list = userCorporationRepository.findByUserIdAndCorporationIdIn(userId, corporationIds);
        return converter.convert(list, dtoClass);
    }
}

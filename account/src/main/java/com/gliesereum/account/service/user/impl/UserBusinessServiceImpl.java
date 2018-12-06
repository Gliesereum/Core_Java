package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserBusinessEntity;
import com.gliesereum.account.model.repository.jpa.user.UserBusinessRepository;
import com.gliesereum.account.service.user.UserBusinessService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
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

    public UserBusinessServiceImpl(UserBusinessRepository repository, DefaultConverter converter) {
        super(repository, converter, DTO_CLASS, ENTITY_CLASS);
    }

    @Autowired
    private UserBusinessRepository repository;

    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            repository.deleteUserBusinessEntityByUserId(id);
        }
    }

    @Override
    public UserBusinessDto getByUserId(UUID id) {
        UserBusinessDto result = null;
        if (id != null) {
            UserBusinessEntity business = repository.getByUserId(id);
            if (business != null) {
                result = converter.convert(business, DTO_CLASS);
            }
        }
        return result;
    }

    @Override
    public boolean KYCPassed(UUID id) {
        boolean result = false;
        if (id != null) {
            UserBusinessDto userBusiness = getByUserId(id);
            if ((userBusiness != null) && (userBusiness.getKYCStatus().equals(KYCStatus.KFC_PASSED))) {
                result = true;
            }
        }
        return result;
    }
}

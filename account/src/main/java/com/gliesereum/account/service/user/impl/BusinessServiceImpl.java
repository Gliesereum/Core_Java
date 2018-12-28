package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.BusinessEntity;
import com.gliesereum.account.model.repository.jpa.user.BusinessRepository;
import com.gliesereum.account.service.user.BusinessService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.BusinessDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_IN_BAN;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Slf4j
@Service
public class BusinessServiceImpl extends DefaultServiceImpl<BusinessDto, BusinessEntity> implements BusinessService {

    private static final Class<BusinessDto> DTO_CLASS = BusinessDto.class;
    private static final Class<BusinessEntity> ENTITY_CLASS = BusinessEntity.class;

    public BusinessServiceImpl(BusinessRepository repository, DefaultConverter converter) {
        super(repository, converter, DTO_CLASS, ENTITY_CLASS);
    }

    @Autowired
    private BusinessRepository repository;

    @Override
    public BusinessDto create(BusinessDto dto) {
        checkUserByStatus();
        return super.create(dto);
    }

    @Override
    public BusinessDto update(BusinessDto dto) {
        checkPermissionUser();
        return super.update(dto);
    }

    @Override
    public void delete(UUID id) {
        checkPermissionUser();
        super.delete(id);
    }

    private void checkUserByStatus() {
        if (SecurityUtil.getUser() == null) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        UserDto user = SecurityUtil.getUser().getUser();
        if (user.getBanStatus().equals(BanStatus.BAN)) {
            throw new ClientException(USER_IN_BAN);
        }
    }

    private void checkPermissionUser() {
        UUID userId = SecurityUtil.getUserId();
    }
}

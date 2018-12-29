package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.BusinessEntity;
import com.gliesereum.account.model.repository.jpa.user.BusinessRepository;
import com.gliesereum.account.service.user.BusinessService;
import com.gliesereum.account.service.user.UserBusinessService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.BusinessDto;
import com.gliesereum.share.common.model.dto.account.user.UserBusinessDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

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

    @Autowired
    private UserBusinessService userBusinessService;

    @Autowired
    private UserService userservice;

    @Override
    public BusinessDto create(BusinessDto dto) {
        BusinessDto result = null;
        checkUserByStatus();
        result = super.create(dto);
        if (result != null) {
            userBusinessService.create(new UserBusinessDto(SecurityUtil.getUserId(), result.getId()));
        }
        return result;
    }

    @Override
    public BusinessDto update(BusinessDto dto) {
        checkCurrentUserForPermissionActionThisBusiness(dto.getId());
        return super.update(dto);
    }

    @Override
    public void delete(UUID id) {
        checkCurrentUserForPermissionActionThisBusiness(id);
        super.delete(id);
    }

    @Override
    public void addUser(UUID idBusiness, UUID idUser) {
        checkActionWithUserFromBusiness(idBusiness, idUser);
        userBusinessService.create(new UserBusinessDto(idUser, idBusiness));
    }

    @Override
    public void removeUser(UUID idBusiness, UUID idUser) {
        checkActionWithUserFromBusiness(idBusiness, idUser);
        UserBusinessDto userBusiness = userBusinessService.getByUserIdAndBusinessId(idUser, idBusiness);
        if (userBusiness == null) {
            throw new ClientException(DONT_FOUND_DEPENDENCY_USER_AND_BUSINESS);
        }
        userBusinessService.delete(userBusiness.getId());
    }

    private void checkActionWithUserFromBusiness(UUID idBusiness, UUID idUser) {
        checkUserByStatus();
        checkCurrentUserForPermissionActionThisBusiness(idBusiness);
        if (userservice.getById(idUser) == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
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

    private void checkCurrentUserForPermissionActionThisBusiness(UUID id) {
        UUID userId = SecurityUtil.getUserId();
    }


}

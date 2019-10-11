package com.gliesereum.karma.service.administrator.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.administator.BusinessAdministratorEntity;
import com.gliesereum.karma.model.repository.jpa.administrator.BusinessAdministratorRepository;
import com.gliesereum.karma.service.administrator.BusinessAdministratorService;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.karma.administrator.BusinessAdministratorDto;
import com.gliesereum.share.common.model.dto.karma.administrator.DetailedBusinessAdministratorDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.BUSINESS_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class BusinessAdministratorServiceImpl extends DefaultServiceImpl<BusinessAdministratorDto, BusinessAdministratorEntity> implements BusinessAdministratorService {

    private static final Class<BusinessAdministratorDto> DTO_CLASS = BusinessAdministratorDto.class;
    private static final Class<BusinessAdministratorEntity> ENTITY_CLASS = BusinessAdministratorEntity.class;

    private final BusinessAdministratorRepository businessAdministratorRepository;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    public BusinessAdministratorServiceImpl(BusinessAdministratorRepository businessAdministratorRepository, DefaultConverter defaultConverter) {
        super(businessAdministratorRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.businessAdministratorRepository = businessAdministratorRepository;
    }

    @Override
    public DetailedBusinessAdministratorDto create(UUID userId, UUID businessId) {
        DetailedBusinessAdministratorDto result = null;
        if (ObjectUtils.allNotNull(userId, businessId)) {
            LiteBusinessDto business = baseBusinessService.getLiteById(businessId);
            if (business == null) {
                throw new ClientException(BUSINESS_NOT_FOUND);
            }
            businessPermissionFacade.checkPermissionByBusiness(businessId, BusinessPermission.BUSINESS_ADMINISTRATION);
            if (!userExchangeService.userIsExist(userId)) {
                throw new ClientException(USER_NOT_FOUND);
            }

            BusinessAdministratorDto businessAdministrator = new BusinessAdministratorDto();
            businessAdministrator.setBusinessId(business.getId());
            businessAdministrator.setCorporationId(business.getCorporationId());
            businessAdministrator.setUserId(userId);
            businessAdministrator = super.create(businessAdministrator);
            result = converter.convert(businessAdministrator, DetailedBusinessAdministratorDto.class);
            setUsers(Arrays.asList(result));

        }
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID businessId) {
        if (ObjectUtils.allNotNull(userId, businessId)) {
            businessPermissionFacade.checkPermissionByBusiness(businessId, BusinessPermission.BUSINESS_ADMINISTRATION);
            businessAdministratorRepository.deleteByUserIdAndBusinessId(userId, businessId);
        }
    }

    @Override
    public boolean existByUserIdBusinessId(UUID userId, UUID businessId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(businessId, userId)) {
            result = businessAdministratorRepository.existsByUserIdAndBusinessId(userId, businessId);
        }
        return result;
    }

    @Override
    public boolean existByUserIdCorporationId(UUID corporationId, UUID userId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(corporationId, userId)) {
            result = businessAdministratorRepository.existsByUserIdAndCorporationId(userId, corporationId);
        }
        return result;
    }

    @Override
    public boolean existByUserId(UUID userId) {
        boolean result = false;
        if (userId != null) {
            result = businessAdministratorRepository.existsByUserId(userId);
        }
        return result;
    }
    
    @Override
    public List<UUID> getUserIdsByBusinessId(UUID businessId) {
        List<UUID> result = null;
        if (businessId != null) {
            List<BusinessAdministratorEntity> entities = businessAdministratorRepository.findAllByBusinessId(businessId);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = entities.stream().map(BusinessAdministratorEntity::getUserId).collect(Collectors.toList());
            }
        }
        return result;
    }
    
    @Override
    public List<DetailedBusinessAdministratorDto> getByBusinessId(UUID businessId) {
        List<DetailedBusinessAdministratorDto> result = null;
        if (businessId != null) {
            List<BusinessAdministratorEntity> entities = businessAdministratorRepository.findAllByBusinessId(businessId);
            result = converter.convert(entities, DetailedBusinessAdministratorDto.class);
            setUsers(result);
        }
        return result;
    }

    @Override
    public List<DetailedBusinessAdministratorDto> getByCorporationId(UUID corporationId) {
        List<DetailedBusinessAdministratorDto> result = null;
        if (corporationId != null) {
            List<BusinessAdministratorEntity> entities = businessAdministratorRepository.findAllByCorporationId(corporationId);
            result = converter.convert(entities, DetailedBusinessAdministratorDto.class);
            setUsers(result);
        }
        return result;
    }

    @Override
    public List<BusinessAdministratorDto> getByUserId(UUID userId) {
        List<BusinessAdministratorDto> result = null;
        if (userId != null) {
            List<BusinessAdministratorEntity> entities = businessAdministratorRepository.findAllByUserId(userId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    private void setUsers(List<DetailedBusinessAdministratorDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            Set<UUID> userIds = list.stream().map(BusinessAdministratorDto::getUserId).collect(Collectors.toSet());
            Map<UUID, PublicUserDto> userMap = userExchangeService.findPublicUserMapByIds(userIds);
            if (MapUtils.isEmpty(userMap)) {
                list.forEach(i -> i.setUser(userMap.get(i.getUserId())));
            }
        }
    }
}

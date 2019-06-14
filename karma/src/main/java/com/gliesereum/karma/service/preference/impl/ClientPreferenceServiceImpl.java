package com.gliesereum.karma.service.preference.impl;

import com.gliesereum.karma.model.entity.preference.ClientPreferenceEntity;
import com.gliesereum.karma.model.repository.jpa.preference.ClientPreferenceRepository;
import com.gliesereum.karma.service.preference.ClientPreferenceService;
import com.gliesereum.karma.service.service.ServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.preference.ClientPreferenceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ClientPreferenceServiceImpl extends DefaultServiceImpl<ClientPreferenceDto, ClientPreferenceEntity> implements ClientPreferenceService {

    private static final Class<ClientPreferenceDto> DTO_CLASS = ClientPreferenceDto.class;
    private static final Class<ClientPreferenceEntity> ENTITY_CLASS = ClientPreferenceEntity.class;

    private final ClientPreferenceRepository clientPreferenceRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    public ClientPreferenceServiceImpl(ClientPreferenceRepository clientPreferenceRepository, DefaultConverter defaultConverter) {
        super(clientPreferenceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.clientPreferenceRepository = clientPreferenceRepository;
    }

    @Override
    @Transactional
    public List<ClientPreferenceDto> addListByServiceIds(List<UUID> serviceIds) {
        SecurityUtil.checkUserByBanStatus();
        UUID clientId = SecurityUtil.getUserId();
        List<ClientPreferenceDto> result = null;
        if (CollectionUtils.isNotEmpty(serviceIds)) {
            List<ClientPreferenceDto> saveDtos = getAllByUserId(clientId);
            if (CollectionUtils.isNotEmpty(saveDtos)) {
                Map<UUID, ClientPreferenceDto> map = saveDtos.stream().collect(Collectors.toMap(ClientPreferenceDto::getId, dto -> dto));
                serviceIds = serviceIds.stream().filter(f -> !map.containsKey(f)).collect(Collectors.toList());
            }
            List<ServiceDto> services = serviceService.getByIds(new HashSet<>(serviceIds));
            if (CollectionUtils.isNotEmpty(services)) {
                List<ClientPreferenceDto> dtos = new ArrayList<>();
                services.forEach(f -> dtos.add(new ClientPreferenceDto(clientId, f.getId(), f.getBusinessCategoryId())));
                result = super.create(dtos);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public ClientPreferenceDto addPreferenceByServiceId(UUID id) {
        SecurityUtil.checkUserByBanStatus();
        UUID clientId = SecurityUtil.getUserId();
        ClientPreferenceDto result = null;
        if (!clientPreferenceRepository.existsByClientIdAndServiceId(clientId, id)) {
            ServiceDto service = serviceService.getById(id);
            if (service != null) {
                result = create(new ClientPreferenceDto(clientId, service.getId(), service.getBusinessCategoryId()));
            }
        }
        return result;
    }

    @Override
    public List<ClientPreferenceDto> getAllByUser() {
        SecurityUtil.checkUserByBanStatus();
        return getAllByUserId(SecurityUtil.getUserId());
    }

    @Override
    public List<ClientPreferenceDto> getAllByUserId(UUID id) {
        List<ClientPreferenceEntity> entities = clientPreferenceRepository.getAllByClientId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<ClientPreferenceDto> getAllByUserIdAndBusinessCategoryIds(UUID userId, List<UUID> businessCategoryIds) {
        List<ClientPreferenceEntity> entities = clientPreferenceRepository.getAllByClientIdAndBusinessCategoryIdIn(userId, businessCategoryIds);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public void deleteByServiceId(UUID id) {
        SecurityUtil.checkUserByBanStatus();
        clientPreferenceRepository.deleteByClientIdAndServiceId(SecurityUtil.getUserId(), id);
    }

    @Override
    @Transactional
    public void deleteAllByUser() {
        SecurityUtil.checkUserByBanStatus();
        clientPreferenceRepository.deleteAllByClientId(SecurityUtil.getUserId());
    }
}

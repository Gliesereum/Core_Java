package com.gliesereum.karma.service.client.impl;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.karma.model.entity.client.ClientEntity;
import com.gliesereum.karma.model.repository.jpa.client.ClientRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.client.ClientService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.service.auditable.impl.AuditableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class ClientServiceImpl extends AuditableServiceImpl<ClientDto, ClientEntity> implements ClientService {

    private static final Class<ClientDto> DTO_CLASS = ClientDto.class;
    private static final Class<ClientEntity> ENTITY_CLASS = ClientEntity.class;

    private final ClientRepository clientRepository;

    @Value("${image-url.user.avatar}")
    private String defaultUserAvatar;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, DefaultConverter defaultConverter) {
        super(clientRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto addNewClient(PublicUserDto user, UUID businessId) {
        ClientDto result = null;
        if (ObjectUtils.allNotNull(user, businessId)) {
            BaseBusinessDto business = baseBusinessService.getById(businessId);
            if (business != null) {
                ClientDto client = new ClientDto();
                client.setBusinessIds(Arrays.asList(businessId));
                client.setCorporationIds((Arrays.asList(business.getCorporationId())));
                client.setAvatarUrl(user.getAvatarUrl());
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setUserId(user.getId());
                client.setMiddleName(user.getMiddleName());
                client.setEmail(user.getEmail());
                client.setPhone(user.getPhone());
                result = addNewClient(client);
            }
        }
        return result;
    }

    @Override
    public ClientDto addNewClient(UserDto user, UUID businessId) {
        ClientDto result = null;
        if (ObjectUtils.allNotNull(user, businessId)) {
            BaseBusinessDto business = baseBusinessService.getById(businessId);
            if (business != null) {
                ClientDto client = new ClientDto();
                client.setBusinessIds(Arrays.asList(businessId));
                client.setCorporationIds((Arrays.asList(business.getCorporationId())));
                client.setUserId(user.getId());
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setMiddleName(user.getMiddleName());
                client.setPhone(user.getPhone());
                client.setAvatarUrl(user.getAvatarUrl());
                result = addNewClient(client);
            }
        }
        return result;
    }

    @Override
    public ClientDto updateClientInfo(UserDto user) {
        ClientDto result = null;
        if ((user != null) && (user.getId() != null)) {
            ClientDto client = this.getByUserId(user.getId());
            if (client != null) {
                client.setMiddleName(user.getMiddleName());
                client.setLastName(user.getLastName());
                client.setFirstName(user.getFirstName());
                client.setAvatarUrl(user.getAvatarUrl());
                result = this.update(client);
            }
        }
        return result;
    }

    @Override
    public Map<UUID, ClientDto> getClientMapByIds(Collection<UUID> ids) {
        Map<UUID, ClientDto> result = new HashMap<>();
        List<ClientDto> clients = getClientByIds(ids);
        if (CollectionUtils.isNotEmpty(clients)) {
            result = clients.stream().collect(Collectors.toMap(ClientDto::getUserId, i -> i));
        }
        return result;
    }

    @Override
    public List<ClientDto> getClientByIds(Collection<UUID> ids) {
        List<ClientDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<ClientEntity> entities = clientRepository.findAllByUserIdIn(ids);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public ClientDto getByUserId(UUID userId) {
        ClientDto result = null;
        if (userId != null) {
            ClientEntity entity = clientRepository.findByUserId(userId);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public ClientDto create(ClientDto dto) {
        setLogoIfNull(dto);
        return super.create(dto);
    }

    @Override
    public ClientDto update(ClientDto dto) {
        setLogoIfNull(dto);
        return super.update(dto);
    }

    private void setLogoIfNull(ClientDto user) {
        if (user != null) {
            if (user.getAvatarUrl() == null) {
                user.setAvatarUrl(defaultUserAvatar);
            }
        }
    }

    private ClientDto addNewClient(ClientDto client) {
        ClientDto result = null;
        ClientDto exist = this.getByUserId(client.getUserId());
        if (exist != null) {
            if (!exist.getBusinessIds().contains(client.getBusinessIds().get(0))) {
                exist.getBusinessIds().add(client.getBusinessIds().get(0));
            }
            if (!exist.getCorporationIds().contains(client.getCorporationIds().get(0))) {
                exist.getCorporationIds().add(client.getCorporationIds().get(0));
            }
            result = this.update(exist);
        } else {
            result = this.create(client);
        }
        return result;
    }
}
package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.karma.model.repository.es.ClientEsRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vitalij
 * @version 1.0
 */
@Service
@Slf4j
public class ClientEsServiceImpl implements ClientEsService {

    private static final  String BUSINESS_IDS = "businessIds";
    private static final String CORPORATION_IDS = "corporationIds";
    private static final  String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";

    private static final Class<ClientDocument> DOCUMENT_CLASS = ClientDocument.class;
    private static final Class<ClientDto> DTO_CLASS = ClientDto.class;

    @Autowired
    private DefaultConverter defaultConverter;

    @Autowired
    private ClientEsRepository clientEsRepository;

    @Autowired
    private BaseBusinessService businessService;

    @Override
    @Transactional
    @Async
    public void addNewClient(PublicUserDto user, UUID businessId) {
        if (ObjectUtils.allNotNull(user, businessId)) {
            BaseBusinessDto business = businessService.getById(businessId);
            if (business != null) {
                ClientDocument client = new ClientDocument();
                client.setBusinessIds(Arrays.asList(businessId.toString()));
                client.setCorporationIds((Arrays.asList(business.getCorporationId().toString())));
                client.setAvatarUrl(user.getAvatarUrl());
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setId(user.getId().toString());
                client.setMiddleName(user.getMiddleName());
                client.setEmail(user.getEmail());
                client.setPhone(user.getPhone());
                addNewClient(client);
            }
        }
    }

    @Override
    @Transactional
    @Async
    public void addNewClient(UserDto user, UUID businessId) {
        if (ObjectUtils.allNotNull(user, businessId)) {
            BaseBusinessDto business = businessService.getById(businessId);
            if (business != null) {
                ClientDocument client = new ClientDocument();
                client.setBusinessIds(Arrays.asList(businessId.toString()));
                client.setCorporationIds((Arrays.asList(business.getCorporationId().toString())));
                client.setId(user.getId().toString());
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setMiddleName(user.getMiddleName());
                client.setPhone(user.getPhone());
                client.setAvatarUrl(user.getAvatarUrl());
                addNewClient(client);
            }
        }
    }

    @Override
    @Async
    public void updateClientInfo(UserDto user) {
        if ((user != null) && (user.getId() != null)) {
            ClientDocument client = clientEsRepository.findById(user.getId().toString()).orElse(null);
            if (client != null) {
                client.setMiddleName(user.getMiddleName());
                client.setLastName(user.getLastName());
                client.setFirstName(user.getFirstName());
                client.setAvatarUrl(user.getAvatarUrl());
                clientEsRepository.save(client);
            }
        }
    }

    @Override
    public Page<ClientDto> getClientsByBusinessIdsOrCorporationIdAndQuery(String query, List<UUID> businessIds, UUID corporationId, Integer page, Integer size) {
        Page<ClientDto> result = null;
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        if (corporationId != null) {
            bq.must(QueryBuilders.termsQuery(CORPORATION_IDS, corporationId.toString()));
        }
        if (CollectionUtils.isNotEmpty(businessIds)) {
            bq.must(QueryBuilders.termsQuery(BUSINESS_IDS, businessIds.stream().map(UUID::toString).collect(Collectors.toList())));
        }
        if (query != null && query.length() >= 3) {
            Map<String, Float> fields = new HashMap<>();
            fields.put(FIRST_NAME, 2.0F);
            fields.put(LAST_NAME, 2.0F);
            fields.put(PHONE, 2.0F);
            bq.must(QueryBuilders.queryStringQuery("*" + query + "*").fields(fields));
        }
        Page<ClientDocument> documents = clientEsRepository.search(bq, PageRequest.of(page, size, Sort.by(FIRST_NAME, LAST_NAME)));
        result = defaultConverter.convert(documents, DTO_CLASS);
        return result;
    }

    private void addNewClient(ClientDocument client) {
        final Optional<ClientDocument> byId = clientEsRepository.findById(client.getId());
        ClientDocument exist = null;
        if (byId.isPresent()) {
            exist = byId.get();
            if (!exist.getBusinessIds().contains(client.getBusinessIds().get(0))) {
                exist.getBusinessIds().add(client.getBusinessIds().get(0));
            }
            if (!exist.getCorporationIds().contains(client.getCorporationIds().get(0))) {
                exist.getCorporationIds().add(client.getCorporationIds().get(0));
            }
        } else {
            exist = client;
        }
        clientEsRepository.save(exist);
    }
}

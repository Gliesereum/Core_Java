package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.karma.model.repository.es.ClientEsRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    @Autowired
    private ClientEsRepository clientEsRepository;

    @Autowired
    private BaseBusinessService businessService;

    private final String BUSINESS_IDS = "businessIds";

    private final String CORPORATION_IDS = "corporationIds";

    private final String FIRST_NAME = "firstName";

    private final String LAST_NAME = "lastName";

    private final String PHONE = "phone";


    @Override
    public Page<ClientDocument> getClientsByBusinessIds(List<UUID> businessIds, Integer page, Integer size) {
        Page<ClientDocument> result = null;
        if (CollectionUtils.isNotEmpty(businessIds)) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.must(QueryBuilders.termsQuery(BUSINESS_IDS, businessIds.stream().map(UUID::toString).collect(Collectors.toList())));
            result = clientEsRepository.search(bq, PageRequest.of(page, size, Sort.by(FIRST_NAME, LAST_NAME)));
        }
        return result;
    }

    @Override
    public Page<ClientDocument> getClientsByCorporationIdsAndAutocompleteQuery(String query, List<UUID> corporationIds, Integer page, Integer size) {
        Page<ClientDocument> result = null;
        if (CollectionUtils.isNotEmpty(corporationIds)) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            List<String> ids = corporationIds.stream().map(UUID::toString).collect(Collectors.toList());
            bq.must(QueryBuilders.termsQuery(CORPORATION_IDS, ids));
            if (query != null && query.length() >= 3) {
                Map<String, Float> fields = new HashMap<>();
                fields.put(FIRST_NAME, 2.0F);
                fields.put(LAST_NAME, 2.0F);
                fields.put(PHONE, 2.0F);
                bq.must(QueryBuilders.queryStringQuery("*" + query + "*").fields(fields));
            }
            result = clientEsRepository.search(bq, PageRequest.of(page, size, Sort.by(FIRST_NAME, LAST_NAME)));
        }
        return result;
    }

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
        if (user != null) {
            ClientDocument client = getClientByUserId(user.getId());
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
    public ClientDocument getClientByUserId(UUID userId) {
        return clientEsRepository.findById(userId.toString()).orElse(null);
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

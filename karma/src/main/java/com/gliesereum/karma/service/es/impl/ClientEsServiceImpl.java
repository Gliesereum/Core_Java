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
    public ClientDocument index(ClientDto client) {
        ClientDocument result = null;
        if (client != null) {
            ClientDocument clientDocument = convert(client);
            result = clientEsRepository.save(clientDocument);
        }
        return result;
    }
    
    @Override
    public List<ClientDocument> index(List<ClientDto> client) {
        List<ClientDocument> result = null;
        if (CollectionUtils.isNotEmpty(client)) {
            List<ClientDocument> documents = client.stream().map(this::convert).collect(Collectors.toList());
            Iterable<ClientDocument> clientDocuments = clientEsRepository.saveAll(documents);
            result = IterableUtils.toList(clientDocuments);
        }
        return result;
    }
    
    @Override
    public Page<ClientDocument> getClientsByBusinessIdsOrCorporationIdAndQuery(String query, List<UUID> businessIds, UUID corporationId, Integer page, Integer size) {
        Page<ClientDocument> result = null;
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
        result = clientEsRepository.search(bq, PageRequest.of(page, size, Sort.by(FIRST_NAME, LAST_NAME)));
        return result;
    }
    
    private ClientDocument convert(ClientDto clientDto) {
        ClientDocument clientDocument = null;
        if (clientDto != null) {
            clientDocument = new ClientDocument();
            clientDocument.setId(clientDto.getId().toString());
            clientDocument.setUserId(clientDto.getUserId().toString());
            if (CollectionUtils.isNotEmpty(clientDto.getCorporationIds())) {
                clientDocument.setCorporationIds(clientDto.getCorporationIds().stream().map(UUID::toString).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(clientDto.getBusinessIds())) {
                clientDocument.setBusinessIds(clientDto.getBusinessIds().stream().map(UUID::toString).collect(Collectors.toList()));
            }
            clientDocument.setFirstName(clientDto.getFirstName());
            clientDocument.setLastName(clientDto.getLastName());
            clientDocument.setMiddleName(clientDto.getMiddleName());
            clientDocument.setPhone(clientDto.getPhone());
            clientDocument.setEmail(clientDto.getEmail());
            clientDocument.setAvatarUrl(clientDto.getAvatarUrl());
        
            clientDocument = clientEsRepository.index(clientDocument);
        }
        return clientDocument;
    }
}

package com.gliesereum.karma.facade.client.impl;

import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.client.ClientService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class ClientFacadeImpl implements ClientFacade {
    
    @Autowired
    private ClientEsService clientEsService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private BaseRecordService recordService;
    
    @Autowired
    private UserExchangeService userExchangeService;
    
    @Autowired
    private BaseBusinessService baseBusinessService;
    
    @Override
    @Async
    public void addNewClient(PublicUserDto user, UUID businessId) {
        ClientDto clientDto = clientService.addNewClient(user, businessId);
        clientEsService.index(clientDto);
    }
    
    @Override
    @Async
    public void addNewClient(UserDto user, UUID businessId) {
        ClientDto clientDto = clientService.addNewClient(user, businessId);
        clientEsService.index(clientDto);
    }
    
    @Override
    @Async
    public void updateClientInfo(UserDto user) {
        ClientDto clientDto = clientService.updateClientInfo(user);
        clientEsService.index(clientDto);
    }
    
    @Override
    public void importClients() {
        log.info("Start import client from record to clientDb");
        Map<UUID, Set<UUID>> result = new HashMap<>();
        int currentPage = 0;
        PageRequest pageRequest = PageRequest.of(currentPage, 100);
        Page<BaseRecordDto> records = recordService.getAll(pageRequest);
        log.info("Get records 100 page: 0");
        parseRecordForImportClient(result, records.getContent());
        int totalPages = records.getTotalPages();
        while (totalPages > currentPage + 1) {
            currentPage++;
            pageRequest = PageRequest.of(currentPage, 100);
            records = recordService.getAll(pageRequest);
            log.info("Get records 100 page: {}", currentPage);
            parseRecordForImportClient(result, records.getContent());
        }
        log.info("Total {} get users from record", result.size());
        if (MapUtils.isNotEmpty(result)) {
            Set<UUID> userIds = result.keySet();
            Map<UUID, PublicUserDto> userMap = userExchangeService.findPublicUserMapByIds(userIds);
            Set<UUID> businessIds = result.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            Map<UUID, LiteBusinessDto> businessMap = baseBusinessService.getLiteBusinessMapByIds(businessIds);
            
            List<Pair<PublicUserDto, List<LiteBusinessDto>>> userPair = result.entrySet().stream().map(i -> {
                PublicUserDto publicUser = userMap.get(i.getKey());
                List<LiteBusinessDto> business = i.getValue().stream().map(businessMap::get).collect(Collectors.toList());
                return Pair.of(publicUser, business);
            }).collect(Collectors.toList());
            clientService.importClients(userPair);
            log.info("Import {} clients", userPair.size());
        }
    }
    
    @Override
    public void indexingClients() {
        log.info("Start Indexing");
        List<ClientDto> clients = clientService.getAll();
        clientEsService.index(clients);
        log.info("Finish indexing {} clients", clients.size());
    }
    
    private void parseRecordForImportClient(Map<UUID, Set<UUID>> clientMap, List<BaseRecordDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            records = records.stream()
                    .filter(i -> i.getClientId() != null)
                    .filter(i -> i.getBusinessId() != null)
                    .collect(Collectors.toList());
            for (BaseRecordDto record : records) {
                clientMap.putIfAbsent(record.getClientId(), new HashSet<>());
                clientMap.get(record.getClientId()).add(record.getBusinessId());
            }
        }
    }
}

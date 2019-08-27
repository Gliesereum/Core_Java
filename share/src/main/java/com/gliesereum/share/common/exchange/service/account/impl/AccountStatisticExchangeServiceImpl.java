package com.gliesereum.share.common.exchange.service.account.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.AccountStatisticExchangeService;
import com.gliesereum.share.common.model.dto.account.statistic.AccountPublicStatisticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class AccountStatisticExchangeServiceImpl implements AccountStatisticExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public AccountStatisticExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public AccountPublicStatisticDto getPublicStatistic() {
        return restTemplate.getForObject(
                exchangeProperties.getAccount().getPublicStatistic(),
                AccountPublicStatisticDto.class);
    }

    @Async
    @Override
    public Future<AccountPublicStatisticDto> getPublicStatisticAsync() {
        return new AsyncResult<>(getPublicStatistic());
    }
}

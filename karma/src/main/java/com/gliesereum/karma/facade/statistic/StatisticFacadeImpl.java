package com.gliesereum.karma.facade.statistic;

import com.gliesereum.karma.controller.business.BaseBusinessController;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.record.RecordServiceService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.AccountStatisticExchangeService;
import com.gliesereum.share.common.model.dto.account.statistic.AccountPublicStatisticDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchDto;
import com.gliesereum.share.common.model.dto.karma.statistic.KarmaPublicStatisticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class StatisticFacadeImpl implements StatisticFacade {

    @Autowired
    private AccountStatisticExchangeService accountStatisticExchangeService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private BaseRecordService baseRecordService;

    @Autowired
    private WorkerService workerService;

    //TODO:refactor on use Auditable
    @Override
    public KarmaPublicStatisticDto getPublicStatistic() {
        KarmaPublicStatisticDto result = new KarmaPublicStatisticDto();
        Future<AccountPublicStatisticDto> accountStatisticFuture = accountStatisticExchangeService.getPublicStatisticAsync();
        result.setBusinessCount(baseBusinessService.count());
        result.setServicePriceCount(servicePriceService.count());
        result.setRecordCount(baseRecordService.count());
        result.setRecordCompletedCount(baseRecordService.countByStatusRecord(StatusRecord.COMPLETED));
        result.setRecordCanceledCount(baseRecordService.countByStatusRecord(StatusRecord.CANCELED));
        result.setRecordPriceSum(baseRecordService.getPriceSum(null));
        result.setRecordCompletedPriceSum(baseRecordService.getPriceSum(searchStatusCompleted()));

        long countBusyWorker = baseRecordService.countBusyWorker(LocalDateTime.now(ZoneId.of("UTC")), StatusRecord.CREATED);
        long countAllWorker = workerService.count();
        long countFreeWorker = countAllWorker - countBusyWorker;
        result.setWorkerBusyCount(countBusyWorker);
        result.setWorkerFreeCount(countFreeWorker);
        try {
            AccountPublicStatisticDto accountPublicStatisticDto = accountStatisticFuture.get();
            result.setUserCount(accountPublicStatisticDto.getUserCount());
            result.setCorporationCount(accountPublicStatisticDto.getCorporationCount());
        } catch (Exception e) {
            log.error("Error while get value from future");
            throw new ClientException(SERVER_ERROR);
        }
        return result;
    }

    private BusinessRecordSearchDto searchStatusCompleted() {
        BusinessRecordSearchDto businessRecordSearchDto = new BusinessRecordSearchDto();
        businessRecordSearchDto.setStatus(Arrays.asList(StatusRecord.COMPLETED));
        return businessRecordSearchDto;
    }
}

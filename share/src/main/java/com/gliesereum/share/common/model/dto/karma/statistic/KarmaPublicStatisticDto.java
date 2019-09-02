package com.gliesereum.share.common.model.dto.karma.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class KarmaPublicStatisticDto {

    private Long corporationCount;

    private Long userCount;

    private Long businessCount;

    private Long servicePriceCount;

    private Long recordCount;

    private Long recordCompletedCount;

    private Long recordCanceledCount;

    private Long recordWaitingCount;

    private Long recordPriceSum;

    private Long recordCompletedPriceSum;

    private Long recordWaitingPriceSum;

    private Long recordCanceledPriceSum;

    private Long workerFreeCount;

    private Long workerBusyCount;
}

package com.gliesereum.share.common.model.dto.account.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class AccountPublicStatisticDto {

    private long userCount;

    private long corporationCount;
}

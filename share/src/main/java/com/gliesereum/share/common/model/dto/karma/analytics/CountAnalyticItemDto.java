package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.record.LiteRecordDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class CountAnalyticItemDto<T extends DefaultDto> {

    private UUID id;

    private String name;

    private T object;

    private Integer recordCount;

    private Double usagePercent;

    private Set<LiteRecordDto> records;
}

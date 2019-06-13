package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.record.LiteRecordDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class FullAnalyticItemDto<T extends DefaultDto> {

    private T object;

    private Set<LiteRecordDto> records = new HashSet<>();
}

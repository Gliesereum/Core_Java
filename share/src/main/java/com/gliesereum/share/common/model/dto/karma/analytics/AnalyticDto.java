package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class AnalyticDto {

    private Map<String, Set<BaseRecordDto>> packages = new HashMap<>();

    private Map<String, Set<BaseRecordDto>> services = new HashMap<>();

    private Map<String, Set<BaseRecordDto>> workingSpaces = new HashMap<>();

    private Map<String, Set<BaseRecordDto>> workers = new HashMap<>();
}
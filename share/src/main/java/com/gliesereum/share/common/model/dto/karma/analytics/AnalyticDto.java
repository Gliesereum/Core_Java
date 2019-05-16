package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.karma.record.LiteRecordDto;
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

    private Map<String, Set<LiteRecordDto>> packages = new HashMap<>();

    private Map<String, Set<LiteRecordDto>> services = new HashMap<>();

    private Map<String, Set<LiteRecordDto>> workingSpaces = new HashMap<>();

    private Map<String, Set<LiteRecordDto>> workers = new HashMap<>();
}
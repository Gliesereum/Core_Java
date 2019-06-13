package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.LiteServicePriceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class FullAnalyticDto {

    private Map<String, FullAnalyticItemDto<LitePackageDto>> packages = new HashMap<>();

    private Map<String, FullAnalyticItemDto<LiteServicePriceDto>> services = new HashMap<>();

    private Map<String, FullAnalyticItemDto<LiteWorkingSpaceDto>> workingSpaces = new HashMap<>();

    private Map<String, FullAnalyticItemDto<LiteWorkerDto>> workers = new HashMap<>();
}

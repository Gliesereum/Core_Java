package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.LiteServicePriceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class CountAnalyticDto {

    private List<CountAnalyticItemDto<LitePackageDto>> packages = new ArrayList<>();

    private List<CountAnalyticItemDto<LiteServicePriceDto>> services = new ArrayList<>();

    private List<CountAnalyticItemDto<LiteWorkingSpaceDto>> workingSpaces = new ArrayList<>();

    private List<CountAnalyticItemDto<LiteWorkerDto>> workers = new ArrayList<>();
}

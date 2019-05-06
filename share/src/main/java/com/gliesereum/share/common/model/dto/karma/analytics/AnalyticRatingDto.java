package com.gliesereum.share.common.model.dto.karma.analytics;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class AnalyticRatingDto {

    private Map<PackageDto, List<BaseRecordDto>> packages;

    private Map<ServicePriceDto, List<BaseRecordDto>> services;
}
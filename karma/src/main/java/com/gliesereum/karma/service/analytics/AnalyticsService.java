package com.gliesereum.karma.service.analytics;

import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticRatingDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface AnalyticsService{

    List<BaseRecordDto> getByAnalyticFilter(AnalyticFilterDto analyticFilter);

    AnalyticRatingDto getPackageAndServiceRating(AnalyticFilterDto analyticFilter);
}    
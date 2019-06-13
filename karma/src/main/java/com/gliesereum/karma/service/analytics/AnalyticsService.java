package com.gliesereum.karma.service.analytics;

import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.CountAnalyticDto;

/**
 * @author vitalij
 * @version 1.0
 */
public interface AnalyticsService {

    AnalyticDto getAnalyticByFilter(AnalyticFilterDto filter);

    CountAnalyticDto getCountAnalyticByFilter(AnalyticFilterDto filter);
}
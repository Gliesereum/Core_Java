package com.gliesereum.karma.controller.analytics;

import com.gliesereum.karma.service.analytics.AnalyticsService;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticRatingDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService service;

    @PostMapping("/records/by-filter")
    public List<BaseRecordDto> getByAnalyticFilter(@RequestBody AnalyticFilterDto analyticFilter) {
        return service.getByAnalyticFilter(analyticFilter);
    }

    @GetMapping("/service/rating")
    public AnalyticRatingDto getPackageAndServiceRating(@RequestBody AnalyticFilterDto analyticFilter) {
        return service.getPackageAndServiceRating(analyticFilter);
    }

}    
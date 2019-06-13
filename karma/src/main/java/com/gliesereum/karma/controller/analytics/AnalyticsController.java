package com.gliesereum.karma.controller.analytics;

import com.gliesereum.karma.service.analytics.AnalyticsService;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.CountAnalyticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService service;

    @PostMapping("/by-filter")
    public AnalyticDto getAnalyticByFilter(@RequestBody AnalyticFilterDto filter) {
        return service.getAnalyticByFilter(filter);
    }

    @PostMapping("/count/by-filter")
    public CountAnalyticDto getCountByFilter(@RequestBody AnalyticFilterDto filter) {
        return service.getCountAnalyticByFilter(filter);
    }
}
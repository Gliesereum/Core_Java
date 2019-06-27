package com.gliesereum.karma.controller.analytics;

import com.gliesereum.karma.service.analytics.AnalyticsService;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.CountAnalyticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CountAnalyticDto getCountByFilter(@RequestBody AnalyticFilterDto filter,
                                             @RequestParam(value = "includeRecord", required = false, defaultValue = "false") boolean includeRecord) {
        return service.getCountAnalyticByFilter(filter, includeRecord);
    }
}
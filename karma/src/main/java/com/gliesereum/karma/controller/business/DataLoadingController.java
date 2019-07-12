package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.DataLoadingService;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/loading")
public class DataLoadingController {

    @Autowired
    private DataLoadingService loadingService;

    @GetMapping("/business")
    public void createBusiness(@QueryParam("rightTop") String rightTop, @QueryParam("leftBottom") String leftBottom) {
        loadingService.createBusiness(rightTop, leftBottom);
    }

    @GetMapping("/records")
    public void createRecords(@RequestParam("businessId") UUID businessId) {
        loadingService.createRecordsByBusinessId(businessId);
    }

    @GetMapping("/client")
    public MapResponse indexClient() {
        loadingService.loadClient();
        return MapResponse.resultTrue();
    }
}
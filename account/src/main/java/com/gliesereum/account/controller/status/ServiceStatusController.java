package com.gliesereum.account.controller.status;

import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/status")
public class ServiceStatusController {

    @GetMapping("/check")
    public MapResponse checkStatus() {
        return new MapResponse("status", "good");
    }
}

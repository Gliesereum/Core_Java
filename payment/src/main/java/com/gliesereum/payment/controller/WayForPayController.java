package com.gliesereum.payment.controller;

import com.gliesereum.payment.service.WayForPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/way-for-pay")
public class WayForPayController {

    @Autowired
    private WayForPayService service;


    @PostMapping("/verify-card")
    private void verifyCard() {
        service.verifyCard();
    }

    @PostMapping("/verify-card-response")
    private void verifyCardResponse(@RequestParam Map<String, String> response) {
        service.verifyCardResponse(response);
    }
}    
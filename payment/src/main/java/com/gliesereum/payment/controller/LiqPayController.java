package com.gliesereum.payment.controller;

import com.gliesereum.payment.service.LiqPayService;
import com.gliesereum.share.common.model.dto.payment.LiqPayRequestDto;
import com.gliesereum.share.common.model.dto.payment.LiqPayStatusRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/liq-pay")
public class LiqPayController {

    @Autowired
    private LiqPayService service;

    @PostMapping("/two-stage-payment")
    private void twoStagePayment(@RequestBody LiqPayRequestDto request) {
        service.twoStagePayment(request);
    }

    @PostMapping("/secure-payment")
    private void securePayment(@RequestBody LiqPayRequestDto request) {
        service.securePayment(request);
    }

    @PostMapping("/return-payment")
    private void returnPayment(@RequestBody LiqPayRequestDto request) {
        service.returnPayment(request);
    }

    @PostMapping("/get-status")
    private void getStatus(@RequestBody LiqPayRequestDto request) {
        service.getStatus(request);
    }

    @PostMapping("/call-back")
    private void callBack(@RequestBody LiqPayStatusRequestDto request) {
        service.callBack(request);
    }

    @GetMapping("/test")
    private void test(){
        service.test();
    }
}

package com.gliesereum.payment.controller.liqpay;

import com.gliesereum.payment.service.liqpay.LiqPayCheckoutService;
import com.gliesereum.share.common.model.dto.payment.liqpay.CheckoutRequestDto;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/liq-pay")
public class LiqPayController {

    @Autowired
    LiqPayCheckoutService checkoutService;

    @PostMapping("/get-checkout-button")
    private String createCheckoutButton(@Valid @RequestBody CheckoutRequestDto request) {
        return checkoutService.createCheckoutButton(request);
    }

    @PostMapping("/get-checkout-link-qr-code")
    private String createCheckoutQrCode(@Valid @RequestBody CheckoutRequestDto request) {
        return checkoutService.createCheckoutQrCode(request);
    }

    @PostMapping("/call-back")
    private void callBack(LiqPayResponseDto response) {
        checkoutService.callBack(response);
    }
}

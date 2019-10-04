package com.gliesereum.payment.controller.liqpay;

import com.gliesereum.payment.service.liqpay.LiqPayCheckoutService;
import com.gliesereum.share.common.model.dto.payment.liqpay.CheckoutRequestDto;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayResponseDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private String createCheckoutLinkQrCode(@Valid @RequestBody CheckoutRequestDto request) {
        return checkoutService.createCheckoutLinkQrCode(request);
    }

    @PostMapping("/get-checkout-qr-code")
    private ResponseEntity<byte[]> createCheckoutQrCode(@Valid @RequestBody CheckoutRequestDto request) {
        byte[] image = checkoutService.createCheckoutQrCode(request);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping("/send-invoice")
    private MapResponse sendInvoice(@Valid @RequestBody CheckoutRequestDto request) {
        checkoutService.sendInvoice(request);
        return new MapResponse("true");
    }

    @PostMapping("/call-back")
    private void callBack(LiqPayResponseDto response) {
        checkoutService.callBack(response);
    }
}

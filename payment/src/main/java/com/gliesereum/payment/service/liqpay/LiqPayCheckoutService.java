package com.gliesereum.payment.service.liqpay;

import com.gliesereum.share.common.model.dto.payment.liqpay.CheckoutRequestDto;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayResponseDto;

import java.util.Map;

public interface LiqPayCheckoutService {

    String createCheckoutButton(CheckoutRequestDto request);

    void callBack(LiqPayResponseDto response);
}

package com.gliesereum.payment.service;

import com.gliesereum.share.common.model.dto.payment.LiqPayRequestDto;
import com.gliesereum.share.common.model.dto.payment.LiqPayStatusRequestDto;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
public interface LiqPayService {

    void twoStagePayment(LiqPayRequestDto request);

    void securePayment(LiqPayRequestDto request);

    void returnPayment(LiqPayRequestDto request);

    void getStatus(LiqPayRequestDto request);

    void callBack(LiqPayStatusRequestDto request);

    void test();

    void testResponse(Map<String, String> response);
}

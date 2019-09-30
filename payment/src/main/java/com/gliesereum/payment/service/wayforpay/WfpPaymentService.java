package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.share.common.model.dto.payment.wayforpay.*;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WfpPaymentService {

    WfpPurchaseResponseModel purchase(WfpPurchaseRequestModel model) throws RuntimeException;

   // void purchaseResponse(Map<String, String> response);
}

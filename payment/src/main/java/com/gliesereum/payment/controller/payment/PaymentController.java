package com.gliesereum.payment.controller.payment;

import com.gliesereum.payment.service.wayforpay.WfpPaymentService;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpPurchaseRequestModel;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpPurchaseResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private WfpPaymentService wfpPaymentService;


    @PostMapping("/way-for-pay/purchase")
    private WfpPurchaseResponseModel wayForPayPurchase(@Valid @RequestBody WfpPurchaseRequestModel model) throws Exception {
        return wfpPaymentService.purchase(model);
    }

    //todo get response for way for pay service to serviceUrl, does not need now
    /*@PostMapping(value = "/way-for-pay/purchase-response")
    private void callBackResponse(@RequestParam Map<String, String> response) {
        wayForPayPaymentService.purchaseResponse(response);
    }*/

}    
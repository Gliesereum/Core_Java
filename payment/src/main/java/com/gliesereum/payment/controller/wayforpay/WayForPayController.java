package com.gliesereum.payment.controller.wayforpay;

import com.gliesereum.payment.service.wayforpay.WayForPayVerifyCardService;
import com.gliesereum.share.common.model.dto.payment.RequestCardInfoDto;
import com.gliesereum.share.common.model.dto.payment.UserCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/way-for-pay")
public class WayForPayController {

    @Autowired
    private WayForPayVerifyCardService verifyCardService;

    @PostMapping("/get-form-verify-card")
    private String getFormVerifyCard() {
        return verifyCardService.getFormVerifyCard();
    }

    @PostMapping("/add-card")
    private UserCardDto addNewCard(@Valid @RequestBody RequestCardInfoDto card) {
        return verifyCardService.addNewCard(card);
    }

    @PostMapping(value = "/verify-card-response")
    private void verifyCardResponse(@RequestParam Map<String, String> response) {
        verifyCardService.verifyCardResponse(response);
    }
}    
package com.gliesereum.payment.controller.card;

import com.gliesereum.payment.service.wayforpay.WfpCardService;
import com.gliesereum.payment.service.wayforpay.WfpVerifyCardService;
import com.gliesereum.share.common.model.dto.payment.payment.RequestCardInfoDto;
import com.gliesereum.share.common.model.dto.payment.payment.UserCardDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private WfpVerifyCardService verifyCardService;

    @Autowired
    private WfpCardService wfpCardService;

    @GetMapping("/my-cards")
    public List<UserCardDto> getMyCards() {
        return wfpCardService.getMyCards();
    }

    @PostMapping("/do-favorite")
    public List<UserCardDto> doFavorite(@RequestParam("idCard") UUID idCard) {
        return wfpCardService.doFavorite(idCard);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        wfpCardService.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/way-for-pay/get-form-verify-card")
    private String getFormVerifyCard() {
        return verifyCardService.getFormVerifyCard();
    }

    @PostMapping("/way-for-pay/add-card")
    private UserCardDto addNewCard(@Valid @RequestBody RequestCardInfoDto card) {
        return verifyCardService.addNewCard(card);
    }

    @PostMapping(value = "/way-for-pay/verify-response")
    private void callBackResponse(@RequestParam Map<String, String> response) {
        verifyCardService.verifyCardResponse(response);
    }

}    
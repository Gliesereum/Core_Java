package com.gliesereum.payment.controller;

import com.gliesereum.payment.service.PaymentCardService;
import com.gliesereum.share.common.model.dto.payment.PaymentCardDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/card")
public class PaymentCardController {

    @Autowired
    private PaymentCardService service;

    @GetMapping("/my-cards")
    public List<PaymentCardDto> getMyCards() {
        return service.getMyCards();
    }

    @PostMapping("/make-favorite")
    public List<PaymentCardDto> makeFavorite(@RequestParam("idCard") UUID idCard) {
        return service.makeFavorite(idCard);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    
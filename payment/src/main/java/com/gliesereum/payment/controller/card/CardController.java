package com.gliesereum.payment.controller.card;

import com.gliesereum.payment.service.wayforpay.WayForPayCardService;
import com.gliesereum.share.common.model.dto.payment.UserCardDto;
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
public class CardController {

    @Autowired
    private WayForPayCardService wayForPayCardService;

    @GetMapping("/my-cards")
    public List<UserCardDto> getMyCards() {
        return wayForPayCardService.getMyCards();
    }

    @PostMapping("/do-favorite")
    public List<UserCardDto> doFavorite(@RequestParam("idCard") UUID idCard) {
        return wayForPayCardService.doFavorite(idCard);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        wayForPayCardService.delete(id);
        return new MapResponse("true");
    }
}    
package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.share.common.model.dto.payment.RequestCardInfoDto;
import com.gliesereum.share.common.model.dto.payment.UserCardDto;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WayForPayVerifyCardService {

    String getFormVerifyCard();

    void verifyCardResponse(Map<String, String> response);

    UserCardDto addNewCard(RequestCardInfoDto card);
}

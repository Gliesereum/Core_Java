package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.share.common.model.dto.payment.payment.RequestCardInfoDto;
import com.gliesereum.share.common.model.dto.payment.payment.UserCardDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCallBackResponseDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCardDto;

import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WfpVerifyCardService {

    void verifyCardResponse(Map<String, String> map);

    String getFormVerifyCard();

    UserCardDto addNewCard(RequestCardInfoDto card);

    WfpCardDto saveCard(WfpCallBackResponseDto response);
}

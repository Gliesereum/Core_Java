package com.gliesereum.share.common.model.dto.payment.liqpay;

import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayActionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayPayType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CheckoutRequestDto {

    private String resultUrl;

    @NotNull
    private String description;

    private LiqPayActionType actionType;

    @NotNull
    private Double amount;

    @NotNull
    private UUID orderId;

    private LiqPayPayType payType;
}

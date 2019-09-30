package com.gliesereum.share.common.model.dto.payment.liqpay;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayActionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayPayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiqPayTransactionDto extends DefaultDto {

    private UUID order_id;

    private LiqPayActionType action;

    private Double agent_commission;

    private Double amount;

    private Double receiver_commission;

    private String card_token;

    private String completion_date;

    private String create_date;

    private String currency;

    private String end_date;

    private String liqpay_order_id;

    private String payment_id;

    private LiqPayPayType paytype;

    private String status;

    private String err_code;

    private String err_description;
}

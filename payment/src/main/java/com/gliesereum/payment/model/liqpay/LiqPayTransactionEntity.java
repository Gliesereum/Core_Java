package com.gliesereum.payment.model.liqpay;

import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayActionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.LiqPayPayType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "liq_pay_transaction")
public class LiqPayTransactionEntity extends DefaultEntity {

    @Column(name = "order_id")
    private UUID order_id;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private LiqPayActionType action;

    @Column(name = "agent_commission")
    private Double agent_commission;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "receiver_commission")
    private Double receiver_commission;

    @Column(name = "card_token")
    private String card_token;

    @Column(name = "completion_date")
    private String completion_date;

    @Column(name = "create_date")
    private String create_date;

    @Column(name = "currency")
    private String currency;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "liqpay_order_id")
    private String liqpay_order_id;

    @Column(name = "payment_id")
    private String payment_id;

    @Column(name = "paytype")
    @Enumerated(EnumType.STRING)
    private LiqPayPayType paytype;

    @Column(name = "status")
    private String status;

    @Column(name = "err_code")
    private String err_code;

    @Column(name = "err_description")
    private String err_description;

}
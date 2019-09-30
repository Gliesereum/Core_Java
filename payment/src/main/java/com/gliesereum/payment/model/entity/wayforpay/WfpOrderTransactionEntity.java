package com.gliesereum.payment.model.entity.wayforpay;

import com.gliesereum.share.common.model.dto.payment.enumerated.WfpPayTransactionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionalStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wfp_order_transaction")
public class WfpOrderTransactionEntity extends DefaultEntity {

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "transactions_status")
    @Enumerated(EnumType.STRING)
    private WfpTransactionalStatus transactionStatus;

    @Column(name = "transactions_type")
    @Enumerated(EnumType.STRING)
    private WfpTransactionType transactionType;

    @Column(name = "pay_transactions_type")
    @Enumerated(EnumType.STRING)
    private WfpPayTransactionType payTransactionType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reason_code")
    private Integer reasonCode;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
}
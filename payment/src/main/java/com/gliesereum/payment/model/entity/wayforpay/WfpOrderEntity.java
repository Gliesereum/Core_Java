package com.gliesereum.payment.model.entity.wayforpay;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Table(name = "wfp_order")
public class WfpOrderEntity extends DefaultEntity {

    @Column(name = "order_reference")
    private UUID orderReference;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "card_pan")
    private String cardPan;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "issuer_bank_country")
    private String issuerBankCountry;

    @Column(name = "issuer_bank_name")
    private String issuerBankName;

    @Column(name = "rec_token")
    private String recToken;

    @Column(name = "payment_system")
    private String paymentSystem;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

}
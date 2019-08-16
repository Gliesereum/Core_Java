package com.gliesereum.payment.model.entity;

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
@Entity
@NoArgsConstructor
@Table(name = "payment_card")
@EqualsAndHashCode(callSuper = true)
public class PaymentCardEntity extends DefaultEntity {

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "rec_token")
    private String recToken;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "is_favorite")
    private boolean favorite;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "card_mask")
    private String cardMask;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reason_code")
    private int reasonCode;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "issuer_bank_country")
    private String issuerBankCountry;

    @Column(name = "issuer_bank_name")
    private String issuerBankName;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "is_verify")
    private boolean verify;

}


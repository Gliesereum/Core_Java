package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonSerializer;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfpPaymentResponseDto {

    private String merchantAccount;

    private UUID orderReference;

    private String merchantSignature;

    private Double amount;

    private String currency;

    private String authCode;

    private String cardPan;

    private String cardType;

    private String issuerBankCountry;

    private String issuerBankName;

    private String recToken;

    private WfpTransactionalStatus transactionStatus;

    private String reason;

    private Integer reasonCode;

    private Double fee;

    private String paymentSystem;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime processingDate;
}
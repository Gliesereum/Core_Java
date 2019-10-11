package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class WfpCallBackResponseDto {

    private String merchantAccount;

    private String orderReference;

    private String merchantSignature;

    private String merchantTransactionType;

    private Double amount;

    private String currency;

    private String authTicket;

    private String authCode;

    private String email;

    private String phone;

    private String cardPan;

    private String cardType;

    private String issuerBankCountry;

    private String issuerBankName;

    private String recToken;

    private String transactionStatus;

    private String reason;

    private Integer reasonCode;

    private Double fee;

    private String paymentSystem;

    private String acquirerBankName;

    private String cardProduct;

    private String clientName;

    private String returnUrl;

    private String repayUrl;

    private String d3AcsUrl;

    private String d3Md;

    private String d3Pareq;

    private String d3TempUrl;

    private String merchantStatus;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime processingDate;
}

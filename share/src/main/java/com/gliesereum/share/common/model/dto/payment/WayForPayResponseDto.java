package com.gliesereum.share.common.model.dto.payment;

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
public class WayForPayResponseDto {

    public String merchantAccount;

    public String orderReference;

    public String merchantSignature;

    public Integer amount;

    public String currency;

    private Long authCode;

    public String email;

    public String phone;

    public String cardPan;

    public String cardType;

    public String issuerBankCountry;

    public String issuerBankName;

    public String recToken;

    public String transactionStatus;

    public String reason;

    public Integer reasonCode;

    public Double fee;

    public String paymentSystem;

    public String acquirerBankName;

    public String cardProduct;

    public String clientName;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    public LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    public LocalDateTime processingDate;
}

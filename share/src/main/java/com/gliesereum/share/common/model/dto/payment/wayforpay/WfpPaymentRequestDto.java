package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfpPaymentRequestDto {

    private String transactionType;

    private String merchantAccount;

    private String merchantAuthType;

    private String merchantDomainName;

    private String merchantTransactionType;

    private String merchantTransactionSecureType;

    private String merchantSignature;

    private Integer apiVersion;

    private String serviceUrl;

    private String orderReference;

    private Double amount;

    private String currency;

    private String recToken;

    private String card;

    private String expMonth;

    private String expYear;

    private String cardCvv;

    private String cardHolder;

    private List<String> productName;

    private List<Double> productPrice;

    private List<Integer> productCount;

    private String clientAccountId;

    private String clientFirstName;

    private String clientLastName;

    private String clientEmail;

    private String clientPhone;

    private String clientCountry;

    private String comment;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime orderDate;
}

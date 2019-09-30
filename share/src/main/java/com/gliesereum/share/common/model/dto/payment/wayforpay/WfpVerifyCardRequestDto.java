package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfpVerifyCardRequestDto {

    private String merchantAccount;

    private String merchantDomainName;

    private String merchantAuthType;

    private String merchantSignature;

    private String merchantTransactionSecureType;

    private String orderReference;

    private Double amount;

    private Integer apiVersion;

    private String currency;

    private String serviceUrl;

    private String transactionType;

    private String card;

    private String expMonth;

    private String expYear;

    private String cardCvv;

    private String cardHolder;

    private String clientFirstName;

    private String clientLastName;

    private String clientCountry;

    private String clientEmail;

    private String clientPhone;

}

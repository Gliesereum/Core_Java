package com.gliesereum.share.common.model.dto.payment;

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
public class WayForPayVerifyCardRequestDto {

    public String merchantAccount;

    public String merchantDomainName;

    public String merchantAuthType;

    public String merchantSignature;

    public String orderReference;

    public Integer amount;

    public Integer apiVersion;

    public String currency;

    public String serviceUrl;

    public String transactionType;

    public String card;

    public String expMonth;

    public String expYear;

    public String cardCvv;

    public String cardHolder;

    public String clientFirstName;

    public String clientLastName;

    public String clientCountry;

    public String clientEmail;

    public String clientPhone;

}

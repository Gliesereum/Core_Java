package com.gliesereum.share.common.model.dto.payment.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class RequestCardInfoDto {

    @NotNull
    private String card;

    @NotNull
    private String expMonth;

    @NotNull
    private String expYear;

    @NotNull
    private String cardCvv;

    @NotNull
    private String cardHolder;
}

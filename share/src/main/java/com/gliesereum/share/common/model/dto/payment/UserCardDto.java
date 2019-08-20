package com.gliesereum.share.common.model.dto.payment;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserCardDto extends DefaultDto {

    private UUID ownerId;

    private String clientName;

    private String cardPan;

    private String cardType;

    private String issuerBankCountry;

    private String issuerBankName;

    private boolean favorite;

    private boolean verify;

}

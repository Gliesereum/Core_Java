package com.gliesereum.share.common.model.dto.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeSecondJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WayForPayCardDto extends DefaultDto {

    private UUID ownerId;

    private String recToken;

    private String clientName;

    private String phone;

    private String email;

    private String cardPan;

    private String cardType;

    private String reason;

    private int reasonCode;

    private String issuerBankCountry;

    private String issuerBankName;

    private boolean favorite;

    private boolean verify;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateTimeSecondJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSecondJsonSerializer.class)
    private LocalDateTime processingDate;

    public WayForPayCardDto(UUID ownerId, boolean verify) {
        this.ownerId = ownerId;
        this.verify = verify;
    }
}

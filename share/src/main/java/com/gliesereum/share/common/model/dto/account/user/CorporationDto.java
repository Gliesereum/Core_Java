package com.gliesereum.share.common.model.dto.account.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vitalij
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CorporationDto extends DefaultDto {

    @Size(min = 2)
    @NotEmpty
    private String name;

    private String description;

    @Size(min = 2)
    @NotEmpty
    private String companyType;

    @Size(min = 6)
    @NotEmpty
    private String address;

    private String logoUrl;

    private String coverUrl;

    @Size(min = 2)
    @NotEmpty
    private String businessActivity;

    private String rcNumber;

    @Size(min = 2)
    @NotEmpty
    private String placeIncorporation;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime dateIncorporation;

    private KYCStatus kYCStatus;

    private VerifiedStatus verifiedStatus;

    private List<CorporationSharedOwnershipDto> corporationSharedOwnerships;

}

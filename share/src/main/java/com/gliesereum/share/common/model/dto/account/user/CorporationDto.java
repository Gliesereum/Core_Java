package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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

    @Size(min = 8, max = 8)
    @NotEmpty
    private String edrpou;

    @Size(min = 6)
    @NotEmpty
    private String address;

    private String logoUrl;

    private String coverUrl;

    private KYCStatus kYCStatus;

    private VerifiedStatus verifiedStatus;

    private List<CorporationSharedOwnershipDto> corporationSharedOwnerships;

}

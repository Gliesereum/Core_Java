package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserBusinessDto extends DefaultDto {

    @Size(min = 2)
    @NotEmpty
    private String name;

    private UUID userId;

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

}

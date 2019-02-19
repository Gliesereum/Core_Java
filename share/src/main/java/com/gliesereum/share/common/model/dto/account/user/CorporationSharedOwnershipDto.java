package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author vitalij
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CorporationSharedOwnershipDto extends DefaultDto {

    @NotNull
    @Max(100)
    @Min(1)
    private Integer share;

    private UUID ownerId;

    private UUID corporationOwnerId;

    private UUID corporationId;

    public CorporationSharedOwnershipDto(Integer share, UUID ownerId, UUID corporationOwnerId, UUID corporationId) {
        this.share = share;
        this.ownerId = ownerId;
        this.corporationOwnerId = corporationOwnerId;
        this.corporationId = corporationId;
    }
}

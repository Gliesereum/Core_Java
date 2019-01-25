package com.gliesereum.share.common.model.dto.account.user;

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
public class UserCorporationDto extends DefaultDto {

    private UUID userId;

    private UUID corporationId;

    public UserCorporationDto(UUID userId, UUID corporationId) {
        this.userId = userId;
        this.corporationId = corporationId;
    }
}

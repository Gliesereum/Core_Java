package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserBusinessDto extends DefaultDto {

    private UUID userId;

    private UUID businessId;

    public UserBusinessDto(UUID userId, UUID businessId) {
        this.userId = userId;
        this.businessId = businessId;
    }
}

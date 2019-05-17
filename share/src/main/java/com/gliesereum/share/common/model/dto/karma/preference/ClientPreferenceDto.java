package com.gliesereum.share.common.model.dto.karma.preference;

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
public class ClientPreferenceDto extends DefaultDto {

    private UUID clientId;

    private UUID serviceId;

    private UUID businessCategoryId;

    public ClientPreferenceDto(UUID clientId, UUID serviceId, UUID businessCategoryId) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.businessCategoryId = businessCategoryId;
    }
}
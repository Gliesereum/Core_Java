package com.gliesereum.share.common.model.dto.karma.service;

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
public class ServiceClassPriceDto extends DefaultDto {

    private UUID priceId;

    private UUID serviceClassId;

    public ServiceClassPriceDto(UUID priceId, UUID serviceClassId) {
        this.priceId = priceId;
        this.serviceClassId = serviceClassId;
    }
}

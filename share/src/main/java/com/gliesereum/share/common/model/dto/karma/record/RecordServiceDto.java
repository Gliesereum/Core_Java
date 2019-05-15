package com.gliesereum.share.common.model.dto.karma.record;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
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
public class RecordServiceDto extends DefaultDto {

    private UUID recordId;

    private UUID serviceId;

    private ServicePriceDto service;

    public RecordServiceDto(UUID recordId, UUID serviceId) {
        this.recordId = recordId;
        this.serviceId = serviceId;
    }
}

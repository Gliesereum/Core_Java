package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecordServiceDto extends DefaultDto {

    private UUID recordId;

    private UUID serviceId;

    public RecordServiceDto(UUID recordId, UUID serviceId) {
        this.recordId = recordId;
        this.serviceId = serviceId;
    }
}

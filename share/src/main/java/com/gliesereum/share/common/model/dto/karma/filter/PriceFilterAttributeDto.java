package com.gliesereum.share.common.model.dto.karma.filter;

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
public class PriceFilterAttributeDto extends DefaultDto {

    private UUID priceId;

    private UUID filterAttributeId;

    public PriceFilterAttributeDto(UUID priceId, UUID filterAttributeId) {
        this.priceId = priceId;
        this.filterAttributeId = filterAttributeId;
    }
}

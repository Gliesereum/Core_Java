package com.gliesereum.share.common.model.dto.karma.car;

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
public class CarFilterAttributeDto extends DefaultDto {

    private UUID carId;

    private UUID filterAttributeId;

    public CarFilterAttributeDto(UUID carId, UUID filterAttributeId) {
        this.carId = carId;
        this.filterAttributeId = filterAttributeId;
    }
}

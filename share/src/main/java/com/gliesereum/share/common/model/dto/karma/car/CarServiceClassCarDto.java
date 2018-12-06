package com.gliesereum.share.common.model.dto.karma.car;

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
public class CarServiceClassCarDto extends DefaultDto {

    private UUID carId;

    private UUID serviceClassId;

    public CarServiceClassCarDto(UUID carId, UUID serviceClassId) {
        this.carId = carId;
        this.serviceClassId = serviceClassId;
    }
}

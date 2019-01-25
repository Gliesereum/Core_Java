package com.gliesereum.share.common.model.dto.karma.carwash;

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
public class CarWashRecordServiceDto extends DefaultDto {

    private UUID carWashRecordId;

    private UUID serviceId;

    public CarWashRecordServiceDto(UUID carWashRecordId, UUID serviceId) {
        this.carWashRecordId = carWashRecordId;
        this.serviceId = serviceId;
    }
}

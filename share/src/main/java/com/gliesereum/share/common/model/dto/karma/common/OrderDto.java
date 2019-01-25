package com.gliesereum.share.common.model.dto.karma.common;

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
public class OrderDto extends DefaultDto {

    private UUID serviceId;

    private UUID recordId;

    private Integer price;

    private Boolean fromPackage;

    public OrderDto(UUID serviceId, UUID recordId, Integer price, Boolean fromPackage) {
        this.serviceId = serviceId;
        this.recordId = recordId;
        this.price = price;
        this.fromPackage = fromPackage;
    }
}

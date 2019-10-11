package com.gliesereum.share.common.model.dto.base.geo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GeoDistanceDto extends GeoPosition {
    
    private Integer distanceMeters;
}

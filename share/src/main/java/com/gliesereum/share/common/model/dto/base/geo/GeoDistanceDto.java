package com.gliesereum.share.common.model.dto.base.geo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class GeoDistanceDto {

    private Double longitude;

    private Double latitude;

    private Integer distanceMeters;
}

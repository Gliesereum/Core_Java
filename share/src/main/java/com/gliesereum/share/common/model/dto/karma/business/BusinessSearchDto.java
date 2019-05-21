package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.base.geo.GeoDistanceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class BusinessSearchDto {

    private UUID targetId;

    private List<UUID> serviceIds;

    private UUID businessCategoryId;

    private List<UUID> businessCategoryIds;

    private GeoDistanceDto geoDistance;
}

package com.gliesereum.share.common.model.dto.karma.business;

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

    //TODO: remove
    //private ServiceType serviceType;

    private UUID businessCategoryId;
}

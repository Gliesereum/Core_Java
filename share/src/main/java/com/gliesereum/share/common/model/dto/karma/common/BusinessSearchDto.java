package com.gliesereum.share.common.model.dto.karma.common;

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

    private UUID carId;

    private List<UUID> serviceIds;
}

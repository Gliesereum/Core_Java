package com.gliesereum.share.common.model.dto.karma.car;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-20
 */

@Data
@NoArgsConstructor
public class CarInfoDto {

    private List<String> serviceClassIds;

    private List<String> filterAttributeIds;
}

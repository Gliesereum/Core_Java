package com.gliesereum.share.common.model.dto.karma.car;

import com.gliesereum.share.common.model.dto.karma.enumerated.CarInteriorType;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-20
 */

@Data
@NoArgsConstructor
public class CarInfoDto {

    private CarInteriorType interiorType;

    private CarType carBody;

    private List<String> serviceClassIds;
}

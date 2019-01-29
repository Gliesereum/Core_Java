package com.gliesereum.share.common.model.dto.karma.car;

import com.gliesereum.share.common.model.dto.karma.common.FilterAttributeDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class CarInfoDto {

    private List<String> serviceClassIds;

    private List<FilterAttributeDto> filterAttributes;
}

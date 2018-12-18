package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/17/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkingSpaceDto extends DefaultDto {

    private UUID workerId;

    private Integer indexNumber;

    private UUID businessServiceId;

    private StatusSpace statusSpace;

    private ServiceType carServiceType;
}

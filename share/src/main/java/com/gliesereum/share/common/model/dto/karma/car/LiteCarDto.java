package com.gliesereum.share.common.model.dto.karma.car;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiteCarDto extends DefaultDto {

    private UUID brandId;

    private UUID modelId;

    private UUID yearId;

    private UUID userId;

    private String registrationNumber;

    private String description;

    private String note;

    private Boolean favorite;

    private List<ServiceClassDto> services = new ArrayList<>();

}

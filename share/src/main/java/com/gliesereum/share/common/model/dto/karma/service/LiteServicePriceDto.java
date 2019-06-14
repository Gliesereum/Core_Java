package com.gliesereum.share.common.model.dto.karma.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiteServicePriceDto extends DefaultDto {

    private String name;

    private String description;

    private Integer price;

    private UUID serviceId;

    private UUID businessId;

    private ServiceDto service;

    private ObjectState objectState;

    private Integer duration;

}

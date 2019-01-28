package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServicePriceDto extends DefaultDto {

    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Integer price;

    private UUID serviceId;

    private UUID businessId;

    private ServiceDto service;

    @NotNull
    @Max(1440)
    @Min(0)
    private Integer duration;

    private List<ServiceClassDto> serviceClass = new ArrayList<>();

    private List<FilterAttributeDto> attributes = new ArrayList<>();
}

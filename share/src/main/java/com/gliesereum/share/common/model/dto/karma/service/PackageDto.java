package com.gliesereum.share.common.model.dto.karma.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.PackageDescriptionDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class PackageDto extends DefaultDto {

    @NotEmpty
    private String name;

    @NotNull
    @Max(100)
    @Min(0)
    private Integer discount;

    @NotNull
    private Integer duration;

    private UUID businessId;

    private ObjectState objectState;

    private List<UUID> servicesIds = new ArrayList<>();

    private List<ServicePriceDto> services = new ArrayList<>();

    private DescriptionReadableDto<PackageDescriptionDto> descriptions = new DescriptionReadableDto<>();

}

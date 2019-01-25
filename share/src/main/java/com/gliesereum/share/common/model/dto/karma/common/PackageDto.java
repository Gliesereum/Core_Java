package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
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

    private UUID corporationServiceId;

    private List<UUID> servicesIds = new ArrayList<>();

    private List<ServicePriceDto> services = new ArrayList<>();
}

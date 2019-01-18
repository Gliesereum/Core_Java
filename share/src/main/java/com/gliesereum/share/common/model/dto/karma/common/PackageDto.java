package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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
public class PackageDto extends DefaultDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private int discount;

    @NotEmpty
    private int duration;

    @NotEmpty
    private UUID corporationServiceId;

    private List<UUID> servicesIds = new ArrayList<>();

    private List<ServicePriceDto> services = new ArrayList<>();
}

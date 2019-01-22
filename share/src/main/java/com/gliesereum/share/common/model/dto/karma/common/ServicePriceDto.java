package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.car.ServiceClassCarDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarInteriorType;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private Integer price;

    @NotNull
    private UUID serviceId;

    @NotNull
    private UUID corporationServiceId;

    private ServiceDto service;

    @NotNull
    @Size(min = 1, max = 1440)
    private Integer duration;

    private List<ServiceClassCarDto> serviceClass = new ArrayList<>();

    private List<CarInteriorType> interiorTypes = new ArrayList<>();

    private List<CarType> carBodies = new ArrayList<>();
}

package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.car.ServiceClassCarDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ServicePriceDto extends DefaultDto {

    private String name;

    @NotNull
    private int price;

    @NotNull
    private UUID serviceId;

    @NotNull
    private UUID userBusinessId;

    private int duration;

    private CarType carBody;

    private List<ServiceClassCarDto> serviceClass = new ArrayList<>();
}

package com.gliesereum.share.common.model.dto.karma.car;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarInteriorType;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import com.gliesereum.share.common.model.dto.karma.enumerated.ColourCarType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CarDto extends DefaultDto {

    @NotNull
    private UUID brandId;

    @NotNull
    private UUID modelId;

    @NotNull
    private UUID yearId;

    private UUID userId;

    private String registrationNumber;

    private String description;

    private String note;

    @NotNull
    private CarInteriorType interior;

    @NotNull
    private CarType carBody;

    @NotNull
    private ColourCarType colour;

    private List<ServiceClassCarDto> services = new ArrayList<>();
}

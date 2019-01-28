package com.gliesereum.share.common.model.dto.karma.car;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    private UUID brandId;

    private UUID modelId;

    private UUID yearId;

    private BrandCarDto brand;

    private ModelCarDto model;

    private YearCarDto year;

    private UUID userId;

    private String registrationNumber;

    private String description;

    private String note;

    private List<ServiceClassDto> services = new ArrayList<>();

    private List<FilterAttributeDto> attributes = new ArrayList<>();
}

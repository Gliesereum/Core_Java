package com.gliesereum.share.common.model.dto.karma.location;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CityDto extends DefaultDto {

    @NotNull
    private String name;

    @NotNull
    private Double centreLatitude;

    @NotNull
    private Double centreLongitude;

    @NotNull
    private UUID countryId;

    private CountryDto country;

    private List<GeoPositionDto> polygonPoints = new ArrayList<>();
}

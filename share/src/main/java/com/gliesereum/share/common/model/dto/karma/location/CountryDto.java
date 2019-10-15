package com.gliesereum.share.common.model.dto.karma.location;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CountryDto extends DefaultDto {

    @NotNull
    private String name;

    @NotNull
    private Double centreLatitude;

    @NotNull
    private Double centreLongitude;

    private List<GeoPositionDto> polygonPoints = new ArrayList<>();
}

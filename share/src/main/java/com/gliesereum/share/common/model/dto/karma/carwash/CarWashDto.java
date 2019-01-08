package com.gliesereum.share.common.model.dto.karma.carwash;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
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
public class CarWashDto extends DefaultDto {

    @NotNull
    private UUID corporationId;

    @NotEmpty
    private String name;

    private String description;

    private String logoUrl;

    @NotEmpty
    @Size(min = 5)
    private String address;

    @NotNull
    @Size(min = 5, max = 13)
    private String phone;

    @Size(min = 5, max = 13)
    private String addPhone;

    @NotNull
    @Max(90)
    @Min(-90)
    private Double latitude;

    @NotNull
    @Max(180)
    @Min(-180)
    private Double longitude;

    private List<WorkTimeDto> workTimes = new ArrayList<>();

    private List<WorkingSpaceDto> spaces = new ArrayList<>();
}

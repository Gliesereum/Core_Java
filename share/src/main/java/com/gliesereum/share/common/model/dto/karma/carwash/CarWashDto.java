package com.gliesereum.share.common.model.dto.karma.carwash;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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
public class CarWashDto extends DefaultDto {

    private UUID userBusinessId;

    @NotEmpty
    private String name;

    private String description;

    private String logoUrl;

    @NotNull
    private String address;

    @NotNull
    @Size(min = 5, max = 13)
    private String phone;

    @Size(min = 5, max = 13)
    private String addPhone;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private List<WorkTimeDto> workTimes = new ArrayList<>();

    private List<WorkingSpaceDto> spaces = new ArrayList<>();
}

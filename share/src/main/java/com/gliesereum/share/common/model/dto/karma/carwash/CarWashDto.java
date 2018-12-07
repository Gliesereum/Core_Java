package com.gliesereum.share.common.model.dto.karma.carwash;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
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
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CarWashDto extends DefaultDto {

    @NotNull
    private UUID userBusinessId;

    @NotNull
    private String name;

    private int countBox;

    private String description;

    @NotNull
    private String address;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private List<WorkTimeDto> workTimes = new ArrayList<>();
}

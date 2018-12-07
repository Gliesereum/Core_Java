package com.gliesereum.share.common.model.dto.karma.common;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkTimeDto extends DefaultDto {

    @NotNull
    private Time from;

    @NotNull
    private Time to;

    @NotNull
    private UUID businessServiceId;

    private String isWork;

    @NotNull
    private ServiceType carServiceType;

    @NotNull
    private DayOfWeek dayOfWeek;
}

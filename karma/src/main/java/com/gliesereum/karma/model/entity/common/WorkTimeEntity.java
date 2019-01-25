package com.gliesereum.karma.model.entity.common;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_time")
public class WorkTimeEntity extends DefaultEntity {

    @Column(name = "from_time")
    private LocalTime from;

    @Column(name = "to_time")
    private LocalTime to;

    @Column(name = "corporation_service_id")
    private UUID corporationServiceId;

    @Column(name = "is_work")
    private Boolean isWork;

    @Column(name = "car_service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType carServiceType;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

}

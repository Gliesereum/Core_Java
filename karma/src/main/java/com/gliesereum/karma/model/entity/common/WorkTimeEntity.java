package com.gliesereum.karma.model.entity.common;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Entity
@Table(name = "work_time")
public class WorkTimeEntity extends DefaultEntity {

    @Column(name = "from_time")
    private Time from;

    @Column(name = "to_time")
    private Time to;

    @Column(name = "business_service_id")
    private UUID businessServiceId;

    @Column(name = "is_work")
    private boolean isWork;

    @Column(name = "car_service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType carServiceType;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

}

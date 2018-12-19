package com.gliesereum.karma.model.document;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-18
 */

@Data
@NoArgsConstructor
public class WorkTimeDocument {

    private LocalTime from;

    private LocalTime to;

    @Field(type = FieldType.Keyword)
    private String businessServiceId;

    @Field(type = FieldType.Boolean)
    private Boolean isWork;

    @Field(type = FieldType.Keyword)
    private ServiceType carServiceType;

    @Field(type = FieldType.Keyword)
    private DayOfWeek dayOfWeek;
}

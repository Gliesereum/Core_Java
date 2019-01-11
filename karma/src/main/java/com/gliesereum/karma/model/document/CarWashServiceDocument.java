package com.gliesereum.karma.model.document;

import com.gliesereum.share.common.model.dto.karma.enumerated.CarInteriorType;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-18
 */

@Data
@NoArgsConstructor
public class CarWashServiceDocument {

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Keyword)
    private String serviceId;

    @Field(type = FieldType.Keyword)
    private String corporationServiceId;

    @Field(type = FieldType.Integer)
    private Integer duration;

    @Field(type = FieldType.Keyword)
    private List<String> serviceClassIds;

    @Field(type = FieldType.Keyword)
    private List<CarType> carBody;

    @Field(type = FieldType.Keyword)
    private List<CarInteriorType> interiorType;
}

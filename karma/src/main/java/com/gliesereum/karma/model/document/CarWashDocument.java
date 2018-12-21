package com.gliesereum.karma.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-18
 */
@Data
@NoArgsConstructor
@Document(indexName = "karma", type = "carwash")
public class CarWashDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String userBusinessId;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer countBox;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Keyword)
    private String address;

    @GeoPointField
    private GeoPoint geoPoint;

    @Field(type = FieldType.Nested)
    private List<CarWashServiceDocument> services;

    @Field(type = FieldType.Nested)
    private List<WorkTimeDocument> workTimes;

}

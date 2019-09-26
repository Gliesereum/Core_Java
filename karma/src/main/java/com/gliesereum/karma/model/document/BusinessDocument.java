package com.gliesereum.karma.model.document;

import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Document(indexName = "karma-business", type = "business")
public class BusinessDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String corporationId;

    @Field(type = FieldType.Text, fielddata = true)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer countBox;

    @Field(type = FieldType.Text, fielddata = true)
    private String description;

    @Field(type = FieldType.Text, fielddata = true)
    private String address;

    @Field(type = FieldType.Keyword)
    private String logoUrl;

    @Field(type = FieldType.Keyword)
    private String coverUrl;

    @Field(type = FieldType.Keyword)
    private String phone;

    @Field(type = FieldType.Keyword)
    private String addPhone;

    @Field(type = FieldType.Keyword)
    private Double latitude;

    @Field(type = FieldType.Keyword)
    private Double longitude;

    @Field(type = FieldType.Keyword)
    private Integer timeZone;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Integer)
    private Integer ratingCount;

    @Field(type = FieldType.Boolean)
    private Boolean businessVerify;

    @GeoPointField
    private GeoPoint geoPoint;

    @Field(type = FieldType.Keyword)
    private UUID businessCategoryId;

    @Field(type = FieldType.Keyword)
    private ObjectState objectState;

    @Field(type = FieldType.Nested)
    private List<BusinessServiceDocument> services;

    @Field(type = FieldType.Nested)
    private List<WorkTimeDocument> workTimes;

    @Field(type = FieldType.Text, fielddata = true)
    private List<String> serviceNames;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Score
    private Float score;

}

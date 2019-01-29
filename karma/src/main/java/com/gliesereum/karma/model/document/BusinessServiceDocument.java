package com.gliesereum.karma.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class BusinessServiceDocument {

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Keyword)
    private String serviceId;

    @Field(type = FieldType.Keyword)
    private String businessId;

    @Field(type = FieldType.Integer)
    private Integer duration;

    @Field(type = FieldType.Keyword)
    private List<String> serviceClassIds;

    @Field(type = FieldType.Keyword)
    private List<String> filterIds;

    @Field(type = FieldType.Keyword)
    private List<String> filterAttributeIds;

}

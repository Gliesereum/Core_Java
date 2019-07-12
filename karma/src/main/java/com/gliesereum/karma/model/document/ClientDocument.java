package com.gliesereum.karma.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Document(indexName = "karma-client", type = "client")
public class ClientDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private List<String> corporationIds;

    @Field(type = FieldType.Keyword)
    private List<String> businessIds;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Text)
    private String middleName;

    @Field(type = FieldType.Text)
    private String phone;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Keyword)
    private String avatarUrl;

}

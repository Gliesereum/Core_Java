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
    private String clientId;

    @Field(type = FieldType.Keyword)
    private List<String> corporationIds;

    @Field(type = FieldType.Keyword)
    private List<String> businessIds;

    @Field(type = FieldType.Keyword)
    private String firstName;

    @Field(type = FieldType.Keyword)
    private String lastName;

    @Field(type = FieldType.Keyword)
    private String middleName;

    @Field(type = FieldType.Keyword)
    private String phone;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String avatarUrl;

    public ClientDocument(String clientId, List<String> corporationIds, List<String> businessIds,
                          String firstName, String lastName, String middleName, String phone,
                          String email, String avatarUrl) {
        this.clientId = clientId;
        this.corporationIds = corporationIds;
        this.businessIds = businessIds;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }
}

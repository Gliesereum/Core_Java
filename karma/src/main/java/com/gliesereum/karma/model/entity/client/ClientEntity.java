package com.gliesereum.karma.model.entity.client;

import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
public class ClientEntity extends AuditableDefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ElementCollection
    @CollectionTable(name = "client_business", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "business_id")
    private List<UUID> businessIds;

    @ElementCollection
    @CollectionTable(name = "client_corporation", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "corporation_id")
    private List<UUID> corporationIds;
}
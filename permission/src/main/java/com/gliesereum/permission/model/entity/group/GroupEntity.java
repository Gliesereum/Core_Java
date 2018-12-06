package com.gliesereum.permission.model.entity.group;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 06/11/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "group")
public class GroupEntity extends DefaultEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "purpose")
    @Enumerated(EnumType.STRING)
    private GroupPurpose purpose;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "inactive_message")
    private String inactiveMessage;

    @Column(name = "parent_group_id")
    private UUID parentGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id", insertable = false, updatable = false)
    private GroupEntity parentGroup;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "group_endpoint",
            joinColumns = { @JoinColumn(name = "group_id", insertable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "endpoint_id", insertable = false, updatable = false) })
    private Set<EndpointEntity> endpoints = new HashSet<>();

}

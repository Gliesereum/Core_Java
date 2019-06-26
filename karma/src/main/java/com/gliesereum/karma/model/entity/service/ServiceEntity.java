package com.gliesereum.karma.model.entity.service;

import com.gliesereum.karma.model.entity.service.descriptions.ServiceDescriptionEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "service")
public class ServiceEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @Column(name = "object_state")
    @Enumerated(EnumType.STRING)
    private ObjectState objectState;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<ServiceDescriptionEntity> descriptions = new HashSet<>();
}

package com.gliesereum.karma.model.entity.service;

import com.gliesereum.karma.model.entity.service.descriptions.ServiceClassDescriptionEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "service_class")
public class ServiceClassEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<ServiceClassDescriptionEntity> descriptions = new HashSet<>();

}

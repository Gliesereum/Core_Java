package com.gliesereum.karma.model.entity.business;

import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_category")
public class BusinessCategoryEntity extends DefaultEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "business_type")
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(name = "active")
    private boolean active;

    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<BusinessCategoryDescriptionEntity> descriptions = new HashSet<>();
}
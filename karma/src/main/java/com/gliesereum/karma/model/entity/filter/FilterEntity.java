package com.gliesereum.karma.model.entity.filter;

import com.gliesereum.karma.model.entity.filter.descriptions.FilterDescriptionEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
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
@Table(name = "filter")
public class FilterEntity extends DefaultEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "title")
    private String title;

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @OneToMany
    @OrderBy("title asc")
    @JoinColumn(name = "filter_id", insertable = false, updatable = false)
    private Set<FilterAttributeEntity> attributes = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<FilterDescriptionEntity> descriptions = new HashSet<>();
}

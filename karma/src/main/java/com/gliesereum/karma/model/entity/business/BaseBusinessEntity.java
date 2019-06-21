package com.gliesereum.karma.model.entity.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business")
public class BaseBusinessEntity extends AbstractBusinessEntity {

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<WorkTimeEntity> workTimes = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "business_id", insertable = false, updatable = false)
    private Set<WorkingSpaceEntity> spaces = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<BusinessDescriptionEntity> descriptions = new HashSet<>();
}

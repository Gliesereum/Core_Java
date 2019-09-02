package com.gliesereum.karma.model.entity.business;

import com.gliesereum.karma.model.entity.business.descriptions.BusinessDescriptionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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

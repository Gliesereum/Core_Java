package com.gliesereum.karma.model.entity.business;

import com.gliesereum.share.common.model.dto.karma.enumerated.StatusSpace;
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
@Table(name = "work_space")
public class WorkingSpaceEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "index_number")
    private Integer indexNumber;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "status_space")
    @Enumerated(EnumType.STRING)
    private StatusSpace statusSpace;

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @OneToMany
    @JoinColumn(name = "work_space_id", insertable = false, updatable = false)
    private Set<WorkerEntity> workers = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<WorkingSpaceDescriptionEntity> descriptions = new HashSet<>();
}

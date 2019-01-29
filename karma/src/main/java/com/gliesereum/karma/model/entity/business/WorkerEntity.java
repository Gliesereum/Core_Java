package com.gliesereum.karma.model.entity.business;

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
@Table(name = "worker")
public class WorkerEntity extends DefaultEntity {

    @Column(name = "worker_id")
    private UUID workerId;

    @Column(name = "position")
    private String position;

    @Column(name = "work_space_id")
    private UUID workingSpaceId;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<WorkTimeEntity> workTimes = new HashSet<>();

}

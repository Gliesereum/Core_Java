package com.gliesereum.karma.model.entity.record;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
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
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "record")
public class BaseRecordEntity extends AbstractRecordEntity {
    
    @Column(name = "working_space_id")
    private UUID workingSpaceId;
    
    @Column(name = "specified_working_space")
    private boolean specifiedWorkingSpace;
    
    @Column(name = "worker_id")
    private UUID workerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", insertable = false, updatable = false)
    private WorkerEntity worker;
    
    @OneToMany
    @JoinColumn(name = "record_id", insertable = false, updatable = false)
    private Set<RecordServiceEntity> services = new HashSet<>();
}

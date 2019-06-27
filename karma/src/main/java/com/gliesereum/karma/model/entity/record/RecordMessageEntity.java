package com.gliesereum.karma.model.entity.record;

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
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "record_message")
public class RecordMessageEntity extends DefaultEntity {

    @Column(name = "message")
    private String message;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<RecordMessageDescriptionEntity> descriptions = new HashSet<>();
}
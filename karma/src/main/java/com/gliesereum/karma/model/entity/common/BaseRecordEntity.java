package com.gliesereum.karma.model.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
}

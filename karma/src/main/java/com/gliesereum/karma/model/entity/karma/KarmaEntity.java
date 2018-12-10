package com.gliesereum.karma.model.entity.karma;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-07
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "karma")
public class KarmaEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "current_value")
    private Long currentValue;

    @Column(name = "fix_value")
    private Long fixValue;

    @Column(name = "level")
    private Integer level;
}

package com.gliesereum.karma.model.entity.bonus;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bonus_score")
@EntityListeners(AuditingEntityListener.class)
public class BonusScoreEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "score")
    private Double score;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany
    @JoinColumn(name = "bonus_score_id", insertable = false, updatable = false)
    private Set<BonusScoreHistoryEntity> history = new HashSet<>();
}
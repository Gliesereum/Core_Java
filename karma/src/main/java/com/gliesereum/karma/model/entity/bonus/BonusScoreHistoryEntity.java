package com.gliesereum.karma.model.entity.bonus;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bonus_score_history")
@EntityListeners(AuditingEntityListener.class)
public class BonusScoreHistoryEntity extends DefaultEntity {

    @Column(name = "bonus_score_id")
    private UUID bonusScoreId;

    @Column(name = "value")
    private Double value;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
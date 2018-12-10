package com.gliesereum.karma.model.entity.comment;

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
@Table(name = "comment")
public class CommentEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "text")
    private String text;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "owner_id")
    private UUID ownerId;
}

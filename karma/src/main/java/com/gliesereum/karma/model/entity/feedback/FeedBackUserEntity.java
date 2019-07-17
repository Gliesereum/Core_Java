package com.gliesereum.karma.model.entity.feedback;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "feedback")
public class FeedBackUserEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID object_id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "working_space_id")
    private UUID workingSpaceId;

    @Column(name = "worker_id")
    private UUID workerId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "date_feedback")
    private LocalDateTime dateFeedback;

    @Column(name = "date_create_object")
    private LocalDateTime dateCreateObject;
}
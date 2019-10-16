package com.gliesereum.lendinggallery.model.entity.offer;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "offer_comment")
public class OfferCommentEntity extends DefaultEntity {

    @Column(name = "comment")
    private String comment;

    @Column(name = "offer_id")
    private UUID offerId;

    @Column(name = "create_by_id")
    private UUID createById;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "state_type")
    @Enumerated(EnumType.STRING)
    private OfferStateType stateType;
}
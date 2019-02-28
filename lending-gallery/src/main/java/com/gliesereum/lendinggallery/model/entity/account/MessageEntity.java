package com.gliesereum.lendinggallery.model.entity.account;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SectionType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Table(name = "message")
public class MessageEntity extends DefaultEntity {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "theme")
    private String theme;

    @Column(name = "message")
    private String message;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "section_type")
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;
}
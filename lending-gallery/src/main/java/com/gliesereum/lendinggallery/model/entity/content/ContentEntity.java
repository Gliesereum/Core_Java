package com.gliesereum.lendinggallery.model.entity.content;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "content")
public class ContentEntity extends DefaultEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
}
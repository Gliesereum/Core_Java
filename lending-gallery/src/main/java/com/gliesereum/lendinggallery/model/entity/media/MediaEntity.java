package com.gliesereum.lendinggallery.model.entity.media;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "media")
public class MediaEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "file_id")
    private UUID fileId;

    @Column(name = "block_media_type")
    @Enumerated(EnumType.STRING)
    private BlockMediaType blockMediaType;

}

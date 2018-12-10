package com.gliesereum.karma.model.entity.media;

import com.gliesereum.share.common.model.dto.karma.enumerated.MediaType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Table(name = "media")
public class MediaEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
}

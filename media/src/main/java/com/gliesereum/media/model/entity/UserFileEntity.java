package com.gliesereum.media.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Entity
@Table(name = "user_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFileEntity extends DefaultEntity {

    @Column(name = "filename")
    private String filename;

    @Column(name = "url")
    private String url;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private UUID userId;

}

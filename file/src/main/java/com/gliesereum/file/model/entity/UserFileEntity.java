package com.gliesereum.file.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
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
@EqualsAndHashCode(callSuper = true)
public class UserFileEntity extends DefaultEntity {

    @Column(name = "filename")
    private String filename;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "url")
    private String url;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "open")
    private Boolean open;

    @Column(name = "crypto")
    private Boolean crypto;

    @ElementCollection
    @CollectionTable(name="user_file_key", joinColumns=@JoinColumn(name="user_file_id"))
    @Column(name="key")
    private List<String> keys;

    @ElementCollection
    @CollectionTable(name="user_file_reader", joinColumns=@JoinColumn(name="user_file_id"))
    @Column(name="reader_id")
    private List<UUID> readerIds;

}

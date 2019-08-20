package com.gliesereum.language.model.entity;

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
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "phrase")
@EqualsAndHashCode(callSuper = true)
public class PhraseEntity extends DefaultEntity {

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "key")
    private String key;

    @Column(name = "label")
    private String label;
}

package com.gliesereum.language.model.entity;

import com.gliesereum.share.common.model.dto.language.enumerated.TextDirection;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "package")
@EqualsAndHashCode(callSuper = true)
public class PackageEntity extends DefaultEntity {

    @Column(name = "module")
    private String module;

    @Column(name = "label")
    private String label;

    @Column(name = "iso_key")
    private String isoKey;

    @Column(name = "icon")
    private String icon;

    @Column(name = "direction")
    @Enumerated(EnumType.STRING)
    private TextDirection direction;

    @OneToMany
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private Set<PhraseEntity> phrases = new HashSet<>();
}

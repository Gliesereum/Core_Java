package com.gliesereum.permission.model.entity.module;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
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
 * @since 06/11/2018
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "module")
public class ModuleEntity extends DefaultEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "inactive_message")
    private String inactiveMessage;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    private Set<EndpointEntity> endpoints = new HashSet<>();

}

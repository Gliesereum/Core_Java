package com.gliesereum.permission.model.entity.application;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "application")
public class ApplicationEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name="application_host", joinColumns=@JoinColumn(name="application_id"))
    @Column(name="host")
    private List<String> hosts;

    @Column(name = "is_active")
    private Boolean isActive;
}
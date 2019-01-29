package com.gliesereum.karma.model.entity.filter;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "filter")
public class FilterEntity extends DefaultEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "title")
    private String title;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToMany
    @JoinColumn(name = "filter_id", insertable = false, updatable = false)
    private Set<FilterAttributeEntity> attributes = new HashSet<>();
}

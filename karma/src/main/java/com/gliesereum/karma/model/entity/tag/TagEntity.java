package com.gliesereum.karma.model.entity.tag;

import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tag")
public class TagEntity extends AuditableDefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
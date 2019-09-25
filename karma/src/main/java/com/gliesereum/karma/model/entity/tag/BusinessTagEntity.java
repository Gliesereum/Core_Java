package com.gliesereum.karma.model.entity.tag;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_tag")
public class BusinessTagEntity extends DefaultEntity {

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "tag_id")
    private UUID tagId;
}
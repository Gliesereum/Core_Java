package com.gliesereum.karma.model.entity.popular;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "business_popular")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessPopularEntity extends DefaultEntity {
    
    @Column(name = "businessId")
    private UUID businessId;
    
    @Column(name = "count")
    private Long count;
}

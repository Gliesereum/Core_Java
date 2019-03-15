package com.gliesereum.lendinggallery.model.entity.offer;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "operation_story")
public class OperationsStoryEntity extends DefaultEntity {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "art_bond_id")
    private UUID artBondId;

    @Column(name ="sum")
    private Double sum;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
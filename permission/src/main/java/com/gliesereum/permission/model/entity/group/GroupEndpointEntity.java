package com.gliesereum.permission.model.entity.group;

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
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "group_endpoint")
public class GroupEndpointEntity extends DefaultEntity {

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "endpoint_id")
    private UUID endpointId;

    //TODO: REMOVE
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "group_id", insertable = false, updatable = false)
//    private GroupEntity group;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "endpoint_id", insertable = false, updatable = false)
//    private EndpointEntity endpoint;
}

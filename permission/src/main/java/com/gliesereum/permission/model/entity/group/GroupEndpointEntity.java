package com.gliesereum.permission.model.entity.group;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 06/11/2018
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
    private UUID endpoint_id;

    //TODO: REMOVE
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "group_id", insertable = false, updatable = false)
//    private GroupEntity group;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "endpoint_id", insertable = false, updatable = false)
//    private EndpointEntity endpoint;
}

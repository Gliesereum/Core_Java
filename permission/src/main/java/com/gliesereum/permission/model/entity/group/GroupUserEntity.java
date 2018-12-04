package com.gliesereum.permission.model.entity.group;

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
@Table(name = "group_user")
public class GroupUserEntity extends DefaultEntity {

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;
}

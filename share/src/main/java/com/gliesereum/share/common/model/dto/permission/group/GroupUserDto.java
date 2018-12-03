package com.gliesereum.share.common.model.dto.permission.group;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupUserDto extends DefaultDto {

    private UUID groupId;

    private UUID userId;


}

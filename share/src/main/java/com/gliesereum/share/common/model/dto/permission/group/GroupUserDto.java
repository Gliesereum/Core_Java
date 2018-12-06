package com.gliesereum.share.common.model.dto.permission.group;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

    @NotNull
    private UUID groupId;

    @NotNull
    private UUID userId;


}

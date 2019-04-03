package com.gliesereum.share.common.model.dto.permission.group;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupUserDto extends DefaultDto {

    @NotNull
    private UUID groupId;

    @NotNull
    private UUID userId;


}

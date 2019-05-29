package com.gliesereum.share.common.model.dto.karma.record;

import com.gliesereum.share.common.model.dto.account.user.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseRecordDto extends AbstractRecordDto {

    private UUID workingSpaceId;

    private UUID workerId;

    private UserDto client;

}

package com.gliesereum.share.common.model.dto.karma.agent;

import com.gliesereum.share.common.model.dto.AuditableDefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentDto extends AuditableDefaultDto {

    private UUID userId;

    private Boolean active;
}

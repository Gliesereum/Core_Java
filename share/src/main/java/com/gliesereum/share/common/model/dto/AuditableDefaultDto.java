package com.gliesereum.share.common.model.dto;

import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuditableDefaultDto extends DefaultDto {

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private LocalDateTime deleteDate;

    private ObjectState objectState;
}

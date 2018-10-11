package com.gliesereum.share.common.model.dto.account;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEmailDto extends DefaultDto {

    private String email;

    private UUID userId;
}

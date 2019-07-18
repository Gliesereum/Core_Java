package com.gliesereum.share.common.model.dto.karma.pincode;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPinCodeDto extends DefaultDto {

    private UUID userId;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]{4}")
    private String pinCode;
}

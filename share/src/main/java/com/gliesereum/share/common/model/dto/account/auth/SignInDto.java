package com.gliesereum.share.common.model.dto.account.auth;

import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class SignInDto {

    @NotEmpty
    private String value;

    @NotEmpty
    private String code;

    @NotNull
    private VerificationType type;

    private String referralCode;
}

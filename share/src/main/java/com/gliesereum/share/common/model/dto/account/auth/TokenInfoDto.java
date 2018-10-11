package com.gliesereum.share.common.model.dto.account.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Data
@NoArgsConstructor
public class TokenInfoDto {

    private String accessToken;

    private String refreshToken;

    private LocalDateTime accessExpirationDate;

    private LocalDateTime refreshExpirationDate;

    private String userId;
}

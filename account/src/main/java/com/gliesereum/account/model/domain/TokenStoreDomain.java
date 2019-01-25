package com.gliesereum.account.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
@RedisHash("tokenStore")
public class TokenStoreDomain implements Serializable {

    @Id
    private String accessToken;

    private String refreshToken;

    private LocalDateTime accessExpirationDate;

    private LocalDateTime refreshExpirationDate;

    private String userId;

}

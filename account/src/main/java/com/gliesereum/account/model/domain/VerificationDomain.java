package com.gliesereum.account.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * @author vitalij
 * @since 10/12/18
 */
@Data
@NoArgsConstructor
@RedisHash("verification")
public class VerificationDomain {

    @Id
    private String id;

    private LocalDateTime createDate;
}

package com.gliesereum.account.service.token.impl;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import com.gliesereum.account.model.repository.redis.TokenStoreRepository;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.share.common.exception.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.TokenExceptionMessage.*;


/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private static final String ACCESS_EXPIRATION_TIME = "token.expirationTime.access";
    private static final String REFRESH_EXPIRATION_TIME = "token.expirationTime.refresh";

    @Autowired
    private TokenStoreRepository tokenStoreRepository;

    @Autowired
    private Environment environment;

    @Override
    public TokenStoreDomain getByAccessToken(String accessToken) {
        return tokenStoreRepository.findById(accessToken).orElse(null);
    }

    @Override
    public TokenStoreDomain getAndVerify(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new ClientException(ACCESS_EMPTY);
        }
        TokenStoreDomain tokenStore = getByAccessToken(accessToken);
        if (tokenStore == null) {
            throw new ClientException(ACCESS_TOKEN_NOT_FOUND);
        }
        if (tokenStore.getAccessExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ClientException(ACCESS_TOKEN_EXPIRED);
        }
        return tokenStore;
    }

    @Override
    public TokenStoreDomain generate(@NotNull String userId) {
        TokenStoreDomain tokenStore = null;
        if (userId != null) {
            tokenStore = new TokenStoreDomain();
            tokenStore.setAccessExpirationDate(generateAccessExpirationDate());
            tokenStore.setRefreshExpirationDate(generateRefreshExpirationDate());
            tokenStore.setAccessToken(UUID.randomUUID().toString());
            tokenStore.setRefreshToken(UUID.randomUUID().toString());
            tokenStore.setUserId(userId);
            tokenStore = tokenStoreRepository.save(tokenStore);
        }
        return tokenStore;
    }

    @Override
    public TokenStoreDomain refresh(String accessToken, String refreshToken) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(refreshToken)) {
            throw new ClientException(ACCESS_REFRESH_EMPTY);
        }
        TokenStoreDomain tokenStore = getByAccessToken(accessToken);
        if (tokenStore == null) {
            throw new ClientException(ACCESS_TOKEN_NOT_FOUND);
        }
        if (!tokenStore.getRefreshToken().equals(refreshToken)) {
            throw new ClientException(PAIR_NOT_VALID);
        }
        if (tokenStore.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ClientException(REFRESH_TOKEN_EXPIRED);
        }
        String userId = tokenStore.getUserId();
        tokenStoreRepository.delete(tokenStore);
        return generate(userId);
    }

    @Override
    public void revoke(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new ClientException(ACCESS_EMPTY);
        }
        TokenStoreDomain tokenStore = getByAccessToken(accessToken);
        if (tokenStore == null) {
            throw new ClientException(ACCESS_TOKEN_NOT_FOUND);
        }
        tokenStoreRepository.delete(tokenStore);
    }

    private LocalDateTime generateAccessExpirationDate() {
        Long accessExpirationTime = environment.getRequiredProperty(ACCESS_EXPIRATION_TIME, Long.class);
        return LocalDateTime.now().plus(accessExpirationTime, ChronoUnit.MILLIS);
    }

    private LocalDateTime generateRefreshExpirationDate() {
        Long refreshExpirationTime = environment.getRequiredProperty(REFRESH_EXPIRATION_TIME, Long.class);
        return LocalDateTime.now().plus(refreshExpirationTime, ChronoUnit.MILLIS);
    }
}

package com.gliesereum.share.common.security.jwt.factory.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import com.gliesereum.share.common.security.model.UserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
public class JwtTokenFactoryImpl implements JwtTokenFactory {

    private static final String IS_ANONYMOUS = "isAnonymous";
    private static final String USER = "user";
    private static final String TOKEN = "token";

    private JwtSecurityProperties jwtSecurityProperties;

    private ObjectMapper objectMapper;

    public JwtTokenFactoryImpl(JwtSecurityProperties jwtSecurityProperties, ObjectMapper objectMapper) {
        this.jwtSecurityProperties = jwtSecurityProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getJwtToken(UserAuthentication userAuthentication) {
        return Jwts.builder()
                .addClaims(toClaims(userAuthentication))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, jwtSecurityProperties.getJwtSecret().getBytes(Charset.forName("UTF-8")))
                .compact();
    }

    @Override
    public UserAuthentication parseJwtToken(String jwtToken) {
        UserAuthentication result = null;
        if (StringUtils.isNotEmpty(jwtToken)) {
            Claims body = Jwts.parser()
                    .setSigningKey(jwtSecurityProperties.getJwtSecret().getBytes(Charset.forName("UTF-8")))
                    .parseClaimsJws(jwtToken)
                    .getBody();
            result = fromClaims(body);
        }
        if (result == null) {
            result = new UserAuthentication();
        }
        return result;
    }

    private Map<String, Object> toClaims(UserAuthentication userAuthentication) {
        Map<String, Object> result = new HashMap<>();
        result.put(IS_ANONYMOUS, userAuthentication.isAnonymous());
        if (!userAuthentication.isAnonymous()) {
            result.put(USER, objectMapper.convertValue(userAuthentication.getUser(), new TypeReference<HashMap<String, Object>>(){}));
            result.put(TOKEN, objectMapper.convertValue(userAuthentication.getTokenInfo(), new TypeReference<HashMap<String, Object>>(){}));
        }
        return result;
    }

    private UserAuthentication fromClaims(Claims claims) {
        UserAuthentication userAuthentication;
        if ((Boolean)claims.get(IS_ANONYMOUS)) {
            userAuthentication = new UserAuthentication();
        } else {
            UserDto user = objectMapper.convertValue(claims.get(USER), new TypeReference<UserDto>(){});
            TokenInfoDto tokenInfo = objectMapper.convertValue(claims.get(TOKEN), new TypeReference<TokenInfoDto>(){});
            userAuthentication = new UserAuthentication(user, tokenInfo);
        }
        return userAuthentication;
    }
}

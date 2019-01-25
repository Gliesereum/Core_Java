package com.gliesereum.share.common.security.model;

import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    private static final List<GrantedAuthority> AUTHENTICATED_AUTHORITY = Arrays.asList(new SimpleGrantedAuthority("USER"));
    private static final List<GrantedAuthority> ANONYMOUS_AUTHORITY = Arrays.asList(new SimpleGrantedAuthority("ANONYMOUS"));

    private TokenInfoDto tokenInfo;

    private String jwtToken;

    private boolean isAnonymous = false;

    public UserAuthentication(UserDto user, TokenInfoDto tokenInfo) {
        super(user, null, AUTHENTICATED_AUTHORITY);
        this.tokenInfo = tokenInfo;
    }

    public UserAuthentication() {
        super(null, null, ANONYMOUS_AUTHORITY);
        this.isAnonymous = true;
    }

    public UserDto getUser() {
        if (!isAnonymous) {
            return (UserDto) getPrincipal();
        }
        return null;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public TokenInfoDto getTokenInfo() {
        return tokenInfo;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
}

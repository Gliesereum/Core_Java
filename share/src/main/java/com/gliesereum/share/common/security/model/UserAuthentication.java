package com.gliesereum.share.common.security.model;

import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.user.BusinessDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    private static final List<GrantedAuthority> AUTHENTICATED_AUTHORITY = Arrays.asList(new SimpleGrantedAuthority("USER"));
    private static final List<GrantedAuthority> ANONYMOUS_AUTHORITY = Arrays.asList(new SimpleGrantedAuthority("ANONYMOUS"));

    private TokenInfoDto tokenInfo;

    private BusinessDto userBusiness;

    private boolean isAnonymous = false;

    public UserAuthentication(UserDto user, TokenInfoDto tokenInfo, BusinessDto userBusiness) {
        super(user, null, AUTHENTICATED_AUTHORITY);
        this.tokenInfo = tokenInfo;
        this.userBusiness = userBusiness;
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

    public TokenInfoDto getTokenInfo() {
        return tokenInfo;
    }

    public BusinessDto getUserBusiness() {
        return userBusiness;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
}

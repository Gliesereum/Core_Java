package com.gliesereum.share.common.security.bearer.filter;

import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.application.filter.ApplicationStore;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Component
@Slf4j
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthExchangeService authService;

    @Autowired
    private ApplicationStore applicationStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserAuthentication userAuthentication = null;
        String header = request.getHeader(securityProperties.getBearerHeader());
        String bearerToken = StringUtils.removeStart(header, securityProperties.getBearerPrefix());
        if (StringUtils.startsWith(header, securityProperties.getBearerPrefix()) && StringUtils.isNotBlank(bearerToken)) {
            bearerToken = bearerToken.trim();
            AuthDto auth = authService.checkAccessToken(bearerToken);
            if (auth != null) {
                userAuthentication = new UserAuthentication(auth.getUser(), auth.getTokenInfo());
            }
        }
        if (userAuthentication == null) {
            userAuthentication = new UserAuthentication();
        }
        ApplicationDto application = applicationStore.getApplication();
        if (application != null) {
            userAuthentication.setApplication(application);
        }
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        filterChain.doFilter(request, response);
    }
}

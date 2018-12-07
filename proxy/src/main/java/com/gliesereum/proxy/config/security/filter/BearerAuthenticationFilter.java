package com.gliesereum.proxy.config.security.filter;

import com.gliesereum.proxy.config.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.auth.AuthService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
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
 * @since 16/10/2018
 */
@Component
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(securityProperties.getBearerHeader());
        String bearerToken = StringUtils.removeStart(header, securityProperties.getBearerPrefix());
        if (StringUtils.startsWith(header, securityProperties.getBearerPrefix()) && StringUtils.isNotBlank(bearerToken)) {
            try {
                bearerToken = bearerToken.trim();
                AuthDto auth = authService.checkAccessToken(bearerToken);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(auth.getUser(), auth.getTokenInfo(), auth.getUserBusiness()));
                    filterChain.doFilter(request, response);
                    return;
                }

            } catch (Exception e) {
                throw e;
            }
        }
        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication());
        filterChain.doFilter(request, response);
    }
}

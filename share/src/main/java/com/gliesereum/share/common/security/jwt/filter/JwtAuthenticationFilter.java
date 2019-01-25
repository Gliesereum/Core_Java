package com.gliesereum.share.common.security.jwt.filter;

import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import com.gliesereum.share.common.security.model.UserAuthentication;
import org.apache.commons.lang3.StringUtils;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSecurityProperties jwtSecurityProperties;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserAuthentication userAuthentication = null;
        String header = request.getHeader(jwtSecurityProperties.getJwtHeader());
        String jwtToken = StringUtils.removeStart(header, jwtSecurityProperties.getJwtPrefix());
        if (StringUtils.startsWith(header, jwtSecurityProperties.getJwtPrefix()) && StringUtils.isNotBlank(jwtToken)) {
            jwtToken = jwtToken.trim();
            userAuthentication = jwtTokenFactory.parseJwtToken(jwtToken);
        }
        if (userAuthentication == null) {
            userAuthentication = new UserAuthentication();
        }
        userAuthentication.setJwtToken(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        filterChain.doFilter(request, response);
    }
}

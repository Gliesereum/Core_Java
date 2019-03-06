package com.gliesereum.socket.config.interceptor;

import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public class RestTemplateBuildJwtInterceptor implements ClientHttpRequestInterceptor {

    private JwtTokenFactory jwtTokenFactory;

    private SecurityProperties securityProperties;

    public RestTemplateBuildJwtInterceptor(JwtTokenFactory jwtTokenFactory, SecurityProperties securityProperties) {
        this.jwtTokenFactory = jwtTokenFactory;
        this.securityProperties = securityProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String jwtToken = jwtTokenFactory.getJwtToken(SecurityUtil.getUser());
        if (StringUtils.isNotEmpty(jwtToken)) {
            request.getHeaders().add(securityProperties.getJwtHeader(), securityProperties.getJwtPrefix() + " " + jwtToken);
        }
        return execution.execute(request, body);
    }
}

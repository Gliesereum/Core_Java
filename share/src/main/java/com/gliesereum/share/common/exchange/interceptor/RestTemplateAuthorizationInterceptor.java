package com.gliesereum.share.common.exchange.interceptor;

import com.gliesereum.share.common.security.properties.JwtSecurityProperties;
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
public class RestTemplateAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private JwtSecurityProperties jwtSecurityProperties;

    public RestTemplateAuthorizationInterceptor(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String jwtToken = SecurityUtil.getJwtToken();
        if (StringUtils.isNotEmpty(jwtToken))
            request.getHeaders().add(jwtSecurityProperties.getJwtHeader(), jwtSecurityProperties.getJwtPrefix() + " " +jwtToken);
        return execution.execute(request, body);
    }
}

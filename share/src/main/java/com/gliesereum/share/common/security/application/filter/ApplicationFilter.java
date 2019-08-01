package com.gliesereum.share.common.security.application.filter;

import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.permission.ApplicationExchangeService;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.share.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_ID_REQUIRED;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_ID_TYPE_NOT_VALID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Component
@Slf4j
public class ApplicationFilter extends OncePerRequestFilter {

    private static final String API_PREFIX = "/api/";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ApplicationExchangeService applicationExchangeService;

    @Autowired
    private ApplicationStore applicationStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        applicationStore.clear();
        String applicationIdString = request.getHeader(securityProperties.getApplicationIdHeader());
        if (StringUtils.isNotBlank(applicationIdString)) {
            applicationIdString = applicationIdString.trim();
            if (!RegexUtil.isUUID(applicationIdString)) {
                throw new ClientException(APPLICATION_ID_TYPE_NOT_VALID);
            }
            UUID applicationId = UUID.fromString(applicationIdString);
            ApplicationDto application = applicationExchangeService.check(applicationId);
            applicationStore.setApplication(application);

        } else if (securityProperties.getApplicationIdHeaderRequired() && !applicationIdNotRequiredForHost(request.getRequestURI())) {
            throw new ClientException(APPLICATION_ID_REQUIRED);
        }
        filterChain.doFilter(request, response);
    }

    private boolean applicationIdNotRequiredForHost(String uri) {
        boolean result = false;
        uri = uri.replaceAll(API_PREFIX, "");
        List<String> hosts = securityProperties.getNotRequiredApplicationIdHosts();
        if (CollectionUtils.isNotEmpty(hosts)) {
            for (String host : hosts) {
                if (result = uri.matches(host)) {
                    break;
                }
            }
        }
        return result;
    }
}

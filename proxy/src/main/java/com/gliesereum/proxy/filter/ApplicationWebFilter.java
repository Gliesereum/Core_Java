package com.gliesereum.proxy.filter;

import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.permission.ApplicationExchangeService;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.share.common.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_ID_REQUIRED;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.APPLICATION_ID_TYPE_NOT_VALID;

/**
 * Reactive replacement for share's servlet ApplicationFilter, which the gateway
 * can no longer use now that it runs on WebFlux.
 *
 * The resolved application is published as an exchange attribute rather than
 * into a request-scoped bean: WebFlux has no request scope, and a request is
 * not pinned to one thread.
 */
@Component
public class ApplicationWebFilter implements WebFilter, Ordered {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 100;

    public static final String APPLICATION_ATTRIBUTE = ApplicationWebFilter.class.getName() + ".APPLICATION";

    private static final String API_PREFIX = "/api/";

    private final SecurityProperties securityProperties;
    private final ApplicationExchangeService applicationExchangeService;

    public ApplicationWebFilter(SecurityProperties securityProperties,
                                ApplicationExchangeService applicationExchangeService) {
        this.securityProperties = securityProperties;
        this.applicationExchangeService = applicationExchangeService;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(securityProperties.getApplicationIdHeader());

        if (StringUtils.isBlank(header)) {
            if (Boolean.TRUE.equals(securityProperties.getApplicationIdHeaderRequired())
                    && !applicationIdNotRequiredForHost(exchange.getRequest().getURI().getRawPath())) {
                return Mono.error(new ClientException(APPLICATION_ID_REQUIRED));
            }
            return chain.filter(exchange);
        }

        String applicationIdString = header.trim();
        if (!RegexUtil.isUUID(applicationIdString)) {
            return Mono.error(new ClientException(APPLICATION_ID_TYPE_NOT_VALID));
        }
        UUID applicationId = UUID.fromString(applicationIdString);

        // The lookup goes over HTTP through a blocking RestTemplate, so it must
        // not run on an event loop thread.
        return Mono.fromCallable(() -> applicationExchangeService.check(applicationId))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(application -> exchange.getAttributes().put(APPLICATION_ATTRIBUTE, application))
                .then(Mono.defer(() -> chain.filter(exchange)));
    }

    static ApplicationDto applicationOf(ServerWebExchange exchange) {
        return exchange.getAttribute(APPLICATION_ATTRIBUTE);
    }

    private boolean applicationIdNotRequiredForHost(String uri) {
        uri = uri.replaceAll(API_PREFIX, "");
        List<String> hosts = securityProperties.getNotRequiredApplicationIdHosts();
        if (CollectionUtils.isEmpty(hosts)) {
            return false;
        }
        for (String host : hosts) {
            if (uri.matches(host)) {
                return true;
            }
        }
        return false;
    }
}

package com.gliesereum.proxy.filter;

import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Reactive replacement for share's servlet BearerAuthenticationFilter.
 *
 * The servlet version parked the result in SecurityContextHolder, a
 * ThreadLocal. A reactive request is not tied to a thread, so the resolved
 * authentication travels as an exchange attribute instead; the only consumer is
 * ServiceAuthorizationGatewayFilter, which mints the downstream JWT from it.
 *
 * An unusable or absent bearer token is not an error here, exactly as before:
 * the request continues as an anonymous UserAuthentication and the downstream
 * service decides what anonymous callers may do.
 */
@Component
public class BearerAuthenticationWebFilter implements WebFilter, Ordered {

    public static final int ORDER = ApplicationWebFilter.ORDER + 10;

    public static final String AUTHENTICATION_ATTRIBUTE =
            BearerAuthenticationWebFilter.class.getName() + ".AUTHENTICATION";

    private final SecurityProperties securityProperties;
    private final AuthExchangeService authService;

    public BearerAuthenticationWebFilter(SecurityProperties securityProperties, AuthExchangeService authService) {
        this.securityProperties = securityProperties;
        this.authService = authService;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return resolveAuthentication(exchange)
                .doOnNext(userAuthentication -> {
                    ApplicationDto application = ApplicationWebFilter.applicationOf(exchange);
                    if (application != null) {
                        userAuthentication.setApplication(application);
                    }
                    exchange.getAttributes().put(AUTHENTICATION_ATTRIBUTE, userAuthentication);
                })
                .then(Mono.defer(() -> chain.filter(exchange)));
    }

    private Mono<UserAuthentication> resolveAuthentication(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(securityProperties.getBearerHeader());
        if (!StringUtils.startsWith(header, securityProperties.getBearerPrefix())) {
            return Mono.just(new UserAuthentication());
        }
        String bearerToken = StringUtils.removeStart(header, securityProperties.getBearerPrefix()).trim();
        if (StringUtils.isBlank(bearerToken)) {
            return Mono.just(new UserAuthentication());
        }
        // Blocking RestTemplate call to account-service; keep it off the event loop.
        return Mono.fromCallable(() -> authService.checkAccessToken(bearerToken))
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toAuthentication)
                .defaultIfEmpty(new UserAuthentication());
    }

    private UserAuthentication toAuthentication(AuthDto auth) {
        return new UserAuthentication(auth.getUser(), auth.getTokenInfo());
    }

    static UserAuthentication authenticationOf(ServerWebExchange exchange) {
        UserAuthentication authentication = exchange.getAttribute(AUTHENTICATION_ATTRIBUTE);
        return authentication != null ? authentication : new UserAuthentication();
    }
}

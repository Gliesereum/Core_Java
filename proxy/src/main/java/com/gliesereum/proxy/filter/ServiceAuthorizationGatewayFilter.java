package com.gliesereum.proxy.filter;

import com.gliesereum.proxy.service.keeper.EndpointKeeperService;
import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Mints the Service-Authorization JWT every service behind the gateway trusts,
 * and enforces the endpoint permission map. Replaces the Zuul PreFilter.
 *
 * Deliberately a GlobalFilter and not a WebFilter: Gateway runs global filters
 * only for requests that matched a route, which is the same scope a Zuul pre
 * filter had. As a WebFilter this would also fire for the gateway's own
 * endpoints such as /api/status, and the permission check would then reject
 * paths it was never meant to see.
 */
@Component
public class ServiceAuthorizationGatewayFilter implements GlobalFilter, Ordered {

    private final SecurityProperties securityProperties;
    private final JwtTokenFactory jwtTokenFactory;
    private final EndpointKeeperService endpointKeeperService;

    public ServiceAuthorizationGatewayFilter(SecurityProperties securityProperties,
                                             JwtTokenFactory jwtTokenFactory,
                                             EndpointKeeperService endpointKeeperService) {
        this.securityProperties = securityProperties;
        this.jwtTokenFactory = jwtTokenFactory;
        this.endpointKeeperService = endpointKeeperService;
    }

    @Override
    public int getOrder() {
        // Before RouteToRequestUrlFilter (10000) and the routing filters, so the
        // header is on the request that actually goes out.
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        UserAuthentication authentication = BearerAuthenticationWebFilter.authenticationOf(exchange);
        String jwtToken = jwtTokenFactory.getJwtToken(authentication);

        if (!Boolean.TRUE.equals(securityProperties.getEndpointKeeperEnable())) {
            return chain.filter(withServiceAuthorization(exchange, jwtToken));
        }

        String path = exchange.getRequest().getURI().getRawPath();
        String method = exchange.getRequest().getMethodValue();
        // The permission map is fetched over HTTP from permission-service.
        return Mono.fromRunnable(() -> endpointKeeperService.checkAccess(authentication, jwtToken, path, method))
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.defer(() -> chain.filter(withServiceAuthorization(exchange, jwtToken))));
    }

    private ServerWebExchange withServiceAuthorization(ServerWebExchange exchange, String jwtToken) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header(securityProperties.getJwtHeader(),
                                securityProperties.getJwtPrefix() + " " + jwtToken)
                        .build())
                .build();
    }
}

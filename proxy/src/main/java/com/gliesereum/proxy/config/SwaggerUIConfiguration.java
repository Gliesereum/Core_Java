package com.gliesereum.proxy.config;

import org.apache.commons.collections4.MapUtils;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
@EnableSwagger2
public class SwaggerUIConfiguration {

    private static final String SWAGGER_API_PATH = "api-docs";

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider(ZuulProperties zuulProperties) {
        return () -> {
            List<SwaggerResource> swaggerResources = new ArrayList<>();
            Map<String, ZuulProperties.ZuulRoute> routes = zuulProperties.getRoutes();
            if (MapUtils.isNotEmpty(routes)) {
                for (ZuulProperties.ZuulRoute route : routes.values()) {
                    SwaggerResource swaggerResource = new SwaggerResource();
                    swaggerResource.setName(route.getId());
                    swaggerResource.setUrl(zuulProperties.getPrefix() + route.getPath().replace("**", SWAGGER_API_PATH));
                    swaggerResources.add(swaggerResource);
                }

            }
            return swaggerResources;
        };
    }
}

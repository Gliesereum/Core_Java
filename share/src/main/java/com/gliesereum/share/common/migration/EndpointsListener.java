package com.gliesereum.share.common.migration;

import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.model.dto.base.enumerated.Method;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointPackDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */
@Component
@Slf4j
public class EndpointsListener implements ApplicationListener<ApplicationReadyEvent> {

    private static boolean isRun = false;

    @Autowired
    private EndpointExchangeService endpointExchangeService;

    @Autowired
    private Environment environment;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Run migrate service endpoints");
        List<EndpointDto> endpoints = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            if (!value.getBeanType().getPackage().getName().contains("gliesereum")) {
                return;
            }
            Set<String> urls = key.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
            if (CollectionUtils.isEmpty(urls) || CollectionUtils.isEmpty(methods)) {
                return;
            }
            for (String url : urls) {
                for (RequestMethod method : methods) {
                    EndpointDto endpoint = new EndpointDto();
                    endpoint.setMethod(Method.valueOf(method.name()));
                    endpoint.setUrl(url.replaceAll("\\{.*\\}", "*"));
                    endpoint.setActive(true);
                    endpoint.setVersion("v1");
                    endpoints.add(endpoint);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(endpoints)) {
            String moduleUrl = environment.getProperty("module-url");
            String moduleName = environment.getProperty("spring.application.name");
            EndpointPackDto endpointPack = new EndpointPackDto();
            endpointPack.setEndpoints(endpoints);
            endpointPack.setModuleUrl(moduleUrl);
            endpointPack.setModuleName(moduleName);
            isRun = true;
            endpointExchangeService.createPack(endpointPack);
        }
        log.info("Finish migrate service endpoints");
    }

}

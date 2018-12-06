package com.gliesereum.share.common.migration;

import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.model.dto.base.enumerated.Method;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointPackDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */
@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean isRun = false;

    @Autowired
    private EndpointExchangeService endpointExchangeService;

    @Autowired
    private Environment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isRun == false) {
            List<EndpointDto> endpoints = new ArrayList<>();
            ApplicationContext applicationContext = event.getApplicationContext();
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
            handlerMethods.forEach((key, value) -> {
                if (value.getBeanType().getPackage().getName().contains("gliesereum")) {
                    Set<String> urls = key.getPatternsCondition().getPatterns();
                    Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
                    if (CollectionUtils.isNotEmpty(urls) && CollectionUtils.isNotEmpty(methods)) {
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
        }
    }
}

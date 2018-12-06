//TODO: to endpoint migrate
//package com.gliesereum.permission.config;
//
//import com.gliesereum.permission.service.endpoint.EndpointService;
//import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
//import com.gliesereum.share.common.migration.EndpointsListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author yvlasiuk
// * @version 1.0
// * @since 2018-12-06
// */
//
//@Configuration
//@ComponentScan(basePackageClasses = EndpointsListener.class)
//public class AutoEndpointListenerConfiguration {
//
//    @Bean
//    public EndpointExchangeService endpointExchangeService(EndpointService endpointService) {
//        return endpointService::createPack;
//    }
//}

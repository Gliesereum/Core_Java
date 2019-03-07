package com.gliesereum.socket.config;

import com.gliesereum.share.common.config.executor.ThreadPoolTaskExecutorDefaultConfiguration;
import com.gliesereum.share.common.config.migration.EndpointListenerDefaultConfiguration;
import com.gliesereum.share.common.migration.EndpointsListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yurii Vlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = {
        EndpointsListener.class,
        ThreadPoolTaskExecutorDefaultConfiguration.class
})
public class EndpointListenerConfiguration {
}

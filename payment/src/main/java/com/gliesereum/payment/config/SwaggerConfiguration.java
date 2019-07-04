package com.gliesereum.payment.config;

import com.gliesereum.share.common.config.swagger.SwaggerDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author vitalij
 */
@Configuration
@ComponentScan(basePackageClasses = SwaggerDefaultConfiguration.class)
public class SwaggerConfiguration {
}

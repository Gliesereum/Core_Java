package com.gliesereum.account.config;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.converter.imp.DefaultConverterImp;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DefaultConverter defaultConverter(ModelMapper modelMapper) {
        return new DefaultConverterImp(modelMapper);
    }
}

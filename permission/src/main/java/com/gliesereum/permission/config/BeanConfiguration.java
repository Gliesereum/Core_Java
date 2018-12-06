package com.gliesereum.permission.config;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.converter.imp.DefaultConverterImp;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public DefaultConverter defaultConverter(ModelMapper modelMapper) {
        return new DefaultConverterImp(modelMapper);
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        localValidatorFactoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }
}

package com.gliesereum.karma.config;

import com.gliesereum.karma.model.entity.business.BusinessDescriptionEntity;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.converter.imp.DefaultConverterImp;
import com.gliesereum.share.common.databind.dto.description.DescriptionConverter;
import com.gliesereum.share.common.model.dto.karma.business.BusinessDescriptionDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vitalij
 * @version 1.0
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        DescriptionConverter.setConverters(modelMapper, BusinessDescriptionDto.class, BusinessDescriptionEntity.class);
        return modelMapper;
    }

    @Bean
    public DefaultConverter defaultConverter(ModelMapper modelMapper) {
        return new DefaultConverterImp(modelMapper);
    }

}

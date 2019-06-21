package com.gliesereum.share.common.databind.dto.description;

import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;
import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.MappingEngineImpl;
import org.modelmapper.internal.util.Types;
import org.modelmapper.spi.MappingContext;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class DescriptionConverter<D extends BaseDescriptionDto, E extends BaseDescriptionEntity> {

    private final Class<D> dtoClass;

    private final Class<E> entityClass;

    public DescriptionConverter(Class<D> dtoClass, Class<E> entityClass) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }


    public static <D extends BaseDescriptionDto, E extends BaseDescriptionEntity> void setConverters(ModelMapper modelMapper, Class<D> dtoClass, Class<E> entityClass) {
        DescriptionConverter<D, E> converter = new DescriptionConverter<>(dtoClass, entityClass);
        modelMapper.addConverter(converter.entityToDtoConverter());
        modelMapper.addConverter(converter.dtoToEntityConverter());
    }

    public Converter<DescriptionReadableDto<D>, Set<E>> dtoToEntityConverter() {
        return new Converter<DescriptionReadableDto<D>, Set<E>>() {
            @Override
            public Set<E> convert(MappingContext<DescriptionReadableDto<D>, Set<E>> context) {
                DescriptionReadableDto<D> source = context.getSource();
                if (MapUtils.isNotEmpty(source)) {
                    Collection<D> values = source.values();
                    if (CollectionUtils.isNotEmpty(values)) {
                        MappingEngineImpl mappingEngine = (MappingEngineImpl) context.getMappingEngine();
                        return values.stream().map(i -> mappingEngine.<D, E>map(i, Types.deProxy(dtoClass), null, TypeToken.of(entityClass), null)).collect(Collectors.toSet());
                    }
                }
                return null;
            }
        };
    }

    public Converter<Set<E>, DescriptionReadableDto<D>> entityToDtoConverter() {
        return new Converter<Set<E>, DescriptionReadableDto<D>>() {
            @Override
            public DescriptionReadableDto<D> convert(MappingContext<Set<E>, DescriptionReadableDto<D>> context) {
                Set<E> source = context.getSource();
                if (CollectionUtils.isNotEmpty(source)) {
                    MappingEngineImpl mappingEngine = (MappingEngineImpl) context.getMappingEngine();
                    Map<LanguageCode, D> map = source.stream()
                            .filter(i -> i.getLanguageCode() != null)
                            .collect(Collectors.toMap(BaseDescriptionEntity::getLanguageCode, i ->
                                    mappingEngine.map(i, Types.deProxy(entityClass), null, TypeToken.of(dtoClass), null)));
                    return new DescriptionReadableDto<>(map);
                }
                return null;
            }
        };

    }

}

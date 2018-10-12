package com.gliesereum.share.common.converter.imp;

import com.gliesereum.share.common.converter.DefaultConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public class DefaultConverterImp implements DefaultConverter {

    private ModelMapper modelMapper;

    public DefaultConverterImp(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <E, T> T convert(E object, Class<T> resultClass) {
        T result = null;
        if (object != null) {
            result = modelMapper.map(object, resultClass);
        }
        return result;
    }

    @Override
    public <E, T> List<T> convert(List<E> objects, Class<T> resultClass) {
        List<T> result = null;
        if (CollectionUtils.isNotEmpty(objects)) {
            result = new ArrayList<>(objects.size());
            for (E object : objects) {
                result.add(convert(object, resultClass));
            }
        }
        return result;
    }
}

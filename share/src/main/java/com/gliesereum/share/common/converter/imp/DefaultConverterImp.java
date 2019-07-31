package com.gliesereum.share.common.converter.imp;

import com.gliesereum.share.common.converter.DefaultConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
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

    @Override
    public <E, T> Set<T> convert(Set<E> objects, Class<T> resultClass) {
        Set<T> result = null;
        if (CollectionUtils.isNotEmpty(objects)) {
            result = new HashSet<>(objects.size());
            for (E object : objects) {
                result.add(convert(object, resultClass));
            }
        }
        return result;
    }

    @Override
    public <E, T> Page<T> convert(Page<E> page, Class<T> resultClass) {
        Page<T> result = null;
        if ((page != null) && CollectionUtils.isNotEmpty(page.getContent())) {
            List<T> content = convert(page.getContent(), resultClass);
            result = new PageImpl<>(content, page.getPageable(), page.getTotalElements());
        }
        return result;
    }
}

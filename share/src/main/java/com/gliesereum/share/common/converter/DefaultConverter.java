package com.gliesereum.share.common.converter;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 10/10/2018
 */
public interface DefaultConverter {

    <E, T> T convert(E object, Class<T> resultClass);

    <E, T> List<T> convert(List<E> objects, Class<T> resultClass);
}

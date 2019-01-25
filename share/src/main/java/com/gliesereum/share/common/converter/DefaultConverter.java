package com.gliesereum.share.common.converter;

import java.util.List;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface DefaultConverter {

    <E, T> T convert(E object, Class<T> resultClass);

    <E, T> List<T> convert(List<E> objects, Class<T> resultClass);

    <E, T> Set<T> convert(Set<E> objects, Class<T> resultClass);
}

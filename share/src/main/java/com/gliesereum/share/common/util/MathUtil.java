package com.gliesereum.share.common.util;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class MathUtil {

    public static Double getOneIfZero(Double value) {
        return (value < 1) ? 1 : value;
    }

    public static Integer getOneIfZero(Integer value) {
        return (value < 1) ? 1 : value;
    }

    public static Long getOneIfZero(Long value) {
        return (value < 1) ? 1 : value;
    }
}

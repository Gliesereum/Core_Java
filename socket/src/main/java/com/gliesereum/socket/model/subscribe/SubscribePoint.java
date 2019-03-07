package com.gliesereum.socket.model.subscribe;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public enum SubscribePoint {

    KARMA("karma", "karmaSocketSubscribeFacadeImp"),
    ;

    private String pointName;

    private String facadeName;

    SubscribePoint(String pointName, String facadeName) {
        this.pointName = pointName;
        this.facadeName = facadeName;
    }

    public static SubscribePoint getByPointName(String pointName) {
        SubscribePoint result = null;
        if (StringUtils.isNotEmpty(pointName)) {
            result = EnumSet.allOf(SubscribePoint.class).stream()
                    .filter(i -> i.getPointName().equals(pointName))
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    public String getPointName() {
        return pointName;
    }

    public String getFacadeName() {
        return facadeName;
    }

}

package com.gliesereum.share.common.util;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class NotificationUtil {

    public static String routingKey(String routing, UUID id) {
        return routing + '.' + id.toString();
    }
}

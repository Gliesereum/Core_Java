package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class SocketExceptionMessage {

    private SocketExceptionMessage() {
        throw new IllegalStateException("Utility class");
    }

    public static final ExceptionMessage INVALID_SUBSCRIBE_STRUCTURE = new ExceptionMessage(1700, 400, "Invalid subscribe structure");
    public static final ExceptionMessage USER_CAN_SUBSCRIBE_ONLY_TO_TOPIC = new ExceptionMessage(1701, 400, "User can subscribe only to topic/**");
    public static final ExceptionMessage SUBSCRIBE_POINT_NOT_FOUND = new ExceptionMessage(1702, 400, "Subscribe point not found");
    public static final ExceptionMessage SUBSCRIBE_POINT_NOT_AVAILABLE = new ExceptionMessage(1703, 400, "Subscribe point not available");
    public static final ExceptionMessage INVALID_ID_TO_SUBSCRIBE = new ExceptionMessage(1704, 400, "Invalid id to subscribe");
    public static final ExceptionMessage USER_CAN_SUBSCRIBE_ONLY_TO_THEMSELVES = new ExceptionMessage(1705, 400, "User can subscribe only to themselves");
    public static final ExceptionMessage USER_CANT_SUBSCRIBE_TO_THIS_BUSINESS = new ExceptionMessage(1706, 400, "User can't subscribe to this business");

}
